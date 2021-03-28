package database;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import models.Game;
import models.Developer;

public class Manager {
	//SQL for the dbms
	private Connection conn;
	private static final String url="jdbc:oracle:thin:@193.6.5.58:1521:XE";
	private static final String classname="oracle.jdbc.driver.OracleDriver";
	private static final String createGameTableSQL="create table Game (id number(2) primary key, name varchar(25) not null, genre varchar(20), release date not null, developerID number(3) not null, foreign key (developerID) references Developer (id))";
	private static final String createDeveloperTableSQL="create table Developer (id number(3) primary key, start_date date not null , name varchar(20) not null, employees number(6) not null)";
	private static final String insertGameSQL="insert into game values(?,?,?,?,?)";
	private static final String selectMaxGameIdSQL="select max(id) from game";
	private static final String insertDeveloperSQL="insert into developer values(?,?,?,?)";
	private static final String selectMaxDeveloperIdSQL="select max(id) from developer";
	private static final String updateGenreWhereGamenameSQL="update game set genre=? where name = ?";
	private static final String updateEmployeesWhereDevnameSQL="update developer set employees=? where name = ?";
	private static final String getGamesByDeveloperSQL="select g.id,g.name,g.genre,g.release,g.developerID from game g inner join developer d on d.id=g.developerID where d.name=?";
	private static final String getGameOrdeByNameSQL="select name, genre from game order by name asc";
	private static final String getDeveloperOrdeByEmployeesSQL="select name, employees from developer order by employees desc";
	private static final String getDeveloperByGameSQL="select d.id, d.start_date, d.name, d.employees from developer d inner join game g on d.id=g.developerID where g.name=?";
	private static final String DELETE_GAME_SQL="DELETE FROM Game WHERE name = ?";
	private static final String DELETE_DEVELOPER_SQL="DELETE FROM Developer WHERE name = ?";
	private static final String SELECT_ALL_TABLES="Select * from game g left outer join developer d on (g.developerID=d.id)";
	
	private static final String CALLABLE_TEST="{? = call fibf(?)}";
	
	//manages the user and its pw and turns of autocommit
	public Manager(String username, String password) throws Exception {
		driverReg();
		this.conn = createConnection(username,password); 
		this.conn.setAutoCommit(false);
	}
	//Registers the driver
	private void driverReg() throws ClassNotFoundException {
			Class.forName(classname);
			System.out.println("Driver Registered!");
	}
	//creates connection to the database
	private Connection createConnection(String username, String password) throws SQLException {	
		Connection conn;
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected Succesfully!");
			return conn;
	}
	// Creates tables in the database
	public void CreateTables() {
		try {  
        PreparedStatement prstmt1 = this.conn.prepareStatement(createDeveloperTableSQL);  
        prstmt1.execute();  
        System.out.println("Developer table was created!\n");  
        PreparedStatement prstmt2 = this.conn.prepareStatement(createGameTableSQL);
        System.out.println("Game table was created!\n");  
        prstmt2.execute(); 
    } catch (Exception ex) {  
        System.err.println("Tables could not be created"+ ex.getMessage());  
    }
}
	//inserts a game into the game table
	public void insertGame(Game game) throws Exception {
		PreparedStatement prstmt = this.conn.prepareStatement(insertGameSQL);
		game.setId(getMaxGameId()+1);
		prstmt.setInt(1, game.getId());
		prstmt.setString(2, game.getName());
		prstmt.setString(3, game.getGenre());
		prstmt.setDate(4, game.getRelease());
		prstmt.setInt(5, game.getDeveloperID());
		
		if(prstmt.execute()) new Exception("Not successful");
	}
	//inserts a developer into the developer table
	public void insertDeveloper(Developer developer) throws Exception {
		PreparedStatement prstmt = this.conn.prepareStatement(insertDeveloperSQL);
		developer.setId(getMaxDevId()+1);
		prstmt.setInt(1, Developer.getId());
		prstmt.setString(3, Developer.getName());
		prstmt.setDate(2, Developer.getStart_date());
		prstmt.setInt(4, Developer.getEmployees());
		
		if(prstmt.execute()) new Exception("Not successful");
	}
	
	private int getMaxDevId() {
		int returnValue=1;
		try {
		ResultSet rs =this.conn.createStatement().executeQuery(selectMaxDeveloperIdSQL);
		rs.next();
		returnValue = rs.getInt(1);
		}catch(SQLException e) {}
		return returnValue;
	}
	private int getMaxGameId() {
		int returnValue=1;
		try {
		ResultSet rs =this.conn.createStatement().executeQuery(selectMaxGameIdSQL);
		rs.next();
		returnValue = rs.getInt(1);
		}catch(SQLException e) {}
		return returnValue;
	}
	public void updateGameGenre(String inputname, String inputgenre) 
	{
		
		try {  
	        PreparedStatement prstmt1 = this.conn.prepareStatement(updateGenreWhereGamenameSQL); 
	    	prstmt1.setString(1, inputgenre);
	    	prstmt1.setString(2, inputname);
	        prstmt1.executeUpdate();   
	        System.out.println("Game updated!\n");  
	        
	    } catch (Exception ex) {  
	        System.err.println("Game cannot be updated!"+ ex.getMessage());  
	    }
		
	}
	public void updateDeveloperEmployees(String inputname, int inputemployees) 
	{
		try {  
	        PreparedStatement prstmt1 = this.conn.prepareStatement(updateEmployeesWhereDevnameSQL); 
	    	prstmt1.setInt(1, inputemployees);
	    	prstmt1.setString(2, inputname);
	        prstmt1.executeUpdate();  
	        System.out.println("Developer updated!\n");  
	        
	    } catch (Exception ex) {  
	        System.err.println("Developer cannot be updated!"+ ex.getMessage());  
	    }
		
		
	}
	//gets games of developer
	public ArrayList<Game> getGamesByDeveloper(String name) throws SQLException{
		ArrayList<Game> result = new ArrayList<Game>();
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getGamesByDeveloperSQL);
		
		prstmt.setString(1, name);
		
		ResultSet rs = prstmt.executeQuery();
		
		while(rs.next()) {
			result.add(new Game(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getDate(4),rs.getInt(5)));
		}
		}catch (SQLException e) {
			return null;
		}
		return result;
	}
	// get a developer of a game from db
	public Developer getDeveloperByGame(String name){
		try {
			PreparedStatement prstmt = this.conn.prepareStatement(getDeveloperByGameSQL);
			prstmt.setString(1, name);
			ResultSet rs = prstmt.executeQuery();
			rs.next();
			return new Developer(rs.getInt(1),rs.getDate(2),rs.getString(3),rs.getInt(4));
			
			}catch(SQLException e) {	}
			
			return null;
		
		
	}
	//orders games by name and writes genre
	public void getGameOrderByName(){
		try {
			PreparedStatement prstmt = this.conn.prepareStatement(getGameOrdeByNameSQL);
			ResultSet rs = prstmt.executeQuery();
		System.out.println("  Name: 			Genre: ");
		System.out.println("-------------------------------------------");
		while(rs.next()) {
			String name = rs.getString(1);
			String genre = rs.getString(2);
			System.out.println(name+"  		 "+genre);
		}
		
		}catch(SQLException e) {	}
		
	}
	//orders developers based on number of emply.
	public void getDeveloperOrderByEmply(){
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getDeveloperOrdeByEmployeesSQL);
		ResultSet rs = prstmt.executeQuery();
		System.out.println("  Name: 			Num. of Emply: ");
		System.out.println("-------------------------------------------");
		while(rs.next()) {
			String name = rs.getString(1);
			int employees  = rs.getInt(2);
			System.out.println(name+"  		 "+employees);
		}
		
		}catch(SQLException e) {	}
		
	}
	//deletes a game based on its name
	public void deleteGameByName(String input) {
		 try {  
				PreparedStatement st = this.conn.prepareStatement(DELETE_GAME_SQL);
		        st.setString(1,input);
		        st.executeUpdate(); 
		    } catch(Exception e) {
		        System.out.println(e);
		    }
	}
	//deletes a developer based on its name
	public void deleteDeveloperByName(String input) {
		 try {  
		PreparedStatement st = this.conn.prepareStatement(DELETE_DEVELOPER_SQL);
        st.setString(1,input);
        st.executeUpdate(); 
    } catch(Exception e) {
        System.out.println(e);
    }
	}
	//selects all tables
	public ArrayList<ArrayList<String>> selectGameAndDevelopers() throws SQLException {
		PreparedStatement prstmt = this.conn.prepareStatement(SELECT_ALL_TABLES);
		ResultSet rs = prstmt.executeQuery();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		while(rs.next()) {
			ArrayList<String> columns = new ArrayList<String>();
			for(int i=1;i<=columnCount;i++) {
				String value = "";
				switch(rsmd.getColumnTypeName(i).toUpperCase()) {
				case "NUMBER":
						value = String.valueOf(rs.getInt(i));
					break;
				case "DATE":
						value = String.valueOf(rs.getDate(i));
						break;
				case "VARCHAR2": 
						value = String.valueOf(rs.getString(i));					
				}
				columns.add(value);
			}
			rows.add(columns);
		}
		return rows;
	}
	//gets tablenames from the user
	public ArrayList<String> getTableNames() throws SQLException{
		ArrayList<String> tableNames= new ArrayList<String>();
		DatabaseMetaData dmd = this.conn.getMetaData();
		Scanner inputs = new Scanner(new InputStreamReader(
		          System.in, Charset.forName("UTF-8")));
		System.out.println("  Name of the Workspace: ");
		String input = inputs.nextLine();
		ResultSet rs = dmd.getTables(null, input, "%", null);
		while(rs.next()) {
			tableNames.add(rs.getString(3));
		}
		rs.close();
		return tableNames;
	}
	//testcall
	public int callableFibf(int number) throws SQLException {
		CallableStatement cs = this.conn.prepareCall(CALLABLE_TEST);
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(2, number);
		cs.execute();
		int result = cs.getInt(1);
		cs.close();
		return result;
	} //disconnects from db
	public void Disconnect() {  
        if (this.conn != null) {  
            try {  
                this.conn.close();  
                System.out.println("Disconnected successfully!");  
            } catch (Exception ex) {  
                System.err.println(ex.getMessage());  
            }  
        }  
    }  


	
	public void commit() throws SQLException {
		this.conn.commit();
	}
	public void rollback() throws SQLException {
		this.conn.rollback();
	}

	public void InsertVaules() {
		int out =0;  
        if (conn != null) {  
            String[] sqlp_Developer = {  
                    "insert into Developer values(1, to_date('2012.08.14','yyyy.mm.dd.'), 'Respawn Ent', '516')",  
                    "insert into Developer values(2, to_date('2008.03.29','yyyy.mm.dd.'), 'Blizzard Ent', '3265')",
                    "insert into Developer values(3, to_date('2001.12.1','yyyy.mm.dd.'), 'Rockstar Games', '4011')",
                    "insert into Developer values(4, to_date('2000.01.14','yyyy.mm.dd.'), 'Team17', '1016')", 
                    "insert into Developer values(5, to_date('2009.09.24','yyyy.mm.dd.'), 'Overkill Software', '432')",
            };  
            String[] sqlp_Game= {  
                    "insert into Game values (1,'Titanfall 1','FPS', to_date('2014.03.11','yyyy.mm.dd.'),'1')",  
                    "insert into Game values (2,'Titanfall 2','FPS', to_date('2016.10.28','yyyy.mm.dd.'),'1')",  
                    "insert into Game values (3,'Worms WMD','Turn-based tactics', to_date('2016.08.23','yyyy.mm.dd.'),'4')",  
                    "insert into Game values (4,'Red Dead Redemption 2','Action-adventure', to_date('2018.10.26','yyyy.mm.dd.'),'3')",  
                    "insert into Game values (5,'Payday The Heist','Arcade FPS', to_date('2011.10.18','yyyy.mm.dd.'),'5')",
                    "insert into Game values (6,'Payday 2','Arcade FPS',to_date('2013.08.13','yyyy.mm.dd.'),'5')",
                    "insert into Game values (7,'Overwatch','Competitive FPS',to_date('2016.05.24','yyyy.mm.dd.'),'2')"};  
              


            Statement s;
			for (int i = 0; i < sqlp_Developer.length; i++) {  
            try {  
                out++;  
                s = conn.createStatement();  
                s.executeUpdate(sqlp_Developer[i]);  
                System.out.println(out + ". Adatsor: Data recorded in Developer table!\n");  
                s.close();  
            } catch (Exception ex) {  
                System.err.println(out + ". Adatsor: Error in Developer table "
                       + ex.getMessage());  
            }  
            }  
            for (int i = 0; i < sqlp_Game.length; i++) {  
                try {  
                    out++;  
                    s = conn.createStatement();  
                    s.executeUpdate(sqlp_Game[i]);  
                    System.out.println(out + ". data recorded in Game table!\n");  
                    s.close();  
                } catch (Exception ex) {  
                    System.err.println(out + ". Adatsor: Error in Game table "
                           + ex.getMessage());  
                }  
            }  
        
        
    }  
    }
	public void writeList(ArrayList<Game> games) {
		for(Game game: games){
            System.out.println(game);
        }		
	}  
	public void writeArrayList(ArrayList<String> result) {
		for(String tablename: result){
            System.out.println(tablename);
        }		
	}  
	public void writeArryArrayList(ArrayList<ArrayList<String>> result) {
		for(ArrayList<String> game: result){
            System.out.println(game);
        }
	}
	public void WriteIntoFile() {
		System.out.println("Writing into out.txt");  
		final String gameSQL="select * from game";
		final String developerSQL="select * from developer";
		try {  
	        PrintWriter outputStream = new PrintWriter
	               (new FileOutputStream(new File("out.txt"), true ));   
	        outputStream.write("Game table:");  
	        outputStream.write("\n");  
	        outputStream.write("ID \t NAME \t GENRE \t RELEASE \t DEVELOPERID");  
	        outputStream.write("\n");  
	        outputStream.write("--------------------------------------------------");  
	        outputStream.write("\n");  
	        PreparedStatement s = this.conn.prepareStatement(gameSQL);  
	        s.executeQuery();  
	        ResultSet rs = s.getResultSet();  
	        while (rs.next()) {  
	              
	            int id = rs.getInt("ID");  
	           String name = rs.getString("NAME");  
	           String genre = rs.getString("GENRE");
	            Date release = rs.getDate("RELEASE");  
	            int developer = rs.getInt("DEVELOPERID");  
	          
	                                  
	            outputStream.write(id+ "\t" + name + "\t" +genre+"\t"
	               +release + "\t"+ developer+"\n");  
	        
	        }  
	        rs.close();
	        outputStream.close();  
	    } catch (Exception ex) {  
	        System.err.println(ex.getMessage());  
	    }
		try {  
	        PrintWriter outputStream = new PrintWriter
	               (new FileOutputStream(new File("out.txt"), true ));   
		outputStream.write("\n Developer table:");  
        outputStream.write("\n");  
        outputStream.write("ID \t CREATION DATE \t NAME \t NUMBER OF EMPLOYEES");  
        outputStream.write("\n");  
        outputStream.write("--------------------------------------------------");  
        outputStream.write("\n");  
        PreparedStatement s2 = this.conn.prepareStatement(developerSQL);  
        s2.executeQuery();  
        ResultSet rs2 = s2.getResultSet();  
        while (rs2.next()) {  
              
            int id = rs2.getInt("ID");  
           Date crdate  = rs2.getDate("START_DATE");  
           String name = rs2.getString("NAME");
            int employees = rs2.getInt("EMPLOYEES");  
          
                                  
            outputStream.write(id+ "\t" + crdate + "\t" +name+"\t"
               +employees+ "\n");
        }  
        rs2.close();
        outputStream.close();  
    } catch (Exception ex) {  
        System.err.println(ex.getMessage());  
    }

		
	}
	 public void Employees() {   
	        Scanner sc = new Scanner(System.in);  
	        Statement s = null;  
	        ResultSet rs = null;  
	  
	        System.out.println("Number of employees: ");  
	        int employ=sc.nextInt();  
	        final String sqlp = "select employees from Developer where employees <= '"+employ+"'";   
	            try {  
	                s = this.conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,  
	                        ResultSet.CONCUR_UPDATABLE);  
	                rs =s.executeQuery(sqlp);  
	                int countemploy=0;  
	                while (rs.next()) {  
	                    int employeesbefor = rs.getInt("employees");  
	                    countemploy++;  
	                    rs.updateInt("employees", (employeesbefor + 10));  
	                    rs.updateRow();  
	                        int employeesafter = rs.getInt("employees");  
	                          
	                          
	                        System.out.println(countemploy +" Developers less than "+employ+" employees.");  
	                        System.out.println(  employeesafter );  
	                      
	                    }  
	                    rs.close();  
	                      
	                  
	            } catch (Exception ex) {  
	                System.err.println(ex.getMessage());  
	            }  sc.close();
	        }  
	    
}
	
