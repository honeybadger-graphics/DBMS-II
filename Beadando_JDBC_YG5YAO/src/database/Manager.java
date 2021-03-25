package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Game;
import models.Developer;

public class Manager {
	private Connection conn;
	private static final String url="jdbc:oracle:thin:@193.6.5.58:1521:XE";
	private static final String classname="oracle.jdbc.driver.OracleDriver";
	//*create table statements *//
	private static final String insertGameSQL="insert into game values(?,?,?,?,?)";
	private static final String selectMaxGameIdSQL="select max(id) from game";
	private static final String insertDeveloperSQL="insert into developer values(?,?,?,?)";
	private static final String selectMaxDeveloperIdSQL="select max(id) from developer";
	private static final String getGamesByDeveloperSQL="select * from game where developer = ?";
	private static final String getGameOrdeByNameSQL="select name, genre from game order by name asc";
	private static final String getDeveloperOrdeByEmployeesSQL="select name, employees from developer order by empleyees desc";
	private static final String getDeveloperByGameSQL="select d.id,d.prodstart_date,d.name,d.emloyees from game g inner join developer d on g.id=d.id where g.id=?";
	private static final String DELETE_GAME_SQL="delete from game where name = ?";
	private static final String DELETE_DEVELOPER_SQL="delete from developer where name = ?";
	private static final String SELECT_ALL_TABLES="Select * from game p left outer join car c on (p.id=c.game)";
	
	private static final String CALLABLE_TEST="{? = call fibf(?)}";
	
	
	public Manager(String username, String password) throws Exception {
		driverReg();
		this.conn = createConnection(username,password); 
		this.conn.setAutoCommit(false);
	}
	
	private void driverReg() throws ClassNotFoundException {
			Class.forName(classname);
			System.out.println("Driver Registered!");
	}
	
	private Connection createConnection(String username, String password) throws SQLException {	
		Connection conn;
			conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected Succesfully!");
			return conn;
	}
	
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
	public void insertDeveloper(Developer developer) throws Exception {
		PreparedStatement prstmt = this.conn.prepareStatement(insertDeveloperSQL);
		developer.setId(getMaxDevId()+1);
		prstmt.setInt(1, developer.getId());
		prstmt.setString(3, developer.getName());
		prstmt.setDate(2, developer.getProdStart_date());
		prstmt.setInt(4, developer.getEmployees());
		
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
	
	public ArrayList<Game> getGamesByDeveloper(Developer developer) throws SQLException{
		ArrayList<Game> result = new ArrayList<Game>();
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getGamesByDeveloperSQL);
		
		prstmt.setInt(1, developer.getId());
		
		ResultSet rs = prstmt.executeQuery();
		
		while(rs.next()) {
			result.add(new Game(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getDate(4),rs.getInt(5)));
		}
		}catch (SQLException e) {
			return null;
		}
		return result;
	}
	
	public Developer getDeveloperByGame(int gameID){
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getDeveloperByGameSQL);
		prstmt.setInt(1, gameID);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		
		return new Developer(rs.getInt(1),rs.getDate(3),rs.getString(2),rs.getInt(4));
		
		}catch(SQLException e) {	}
		
		return null;
	}
	public void getGameOrderByName(){
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getGameOrdeByNameSQL);
		ResultSet rs = prstmt.executeQuery();
		System.out.print("  Name: 			Genre: ");
		System.out.print("-------------------------------------------");
		while(rs.next()) {
			String name = rs.getString(2);
			String genre = rs.getString(3);
			System.out.print(name+"  		 "+genre);
		}
		
		}catch(SQLException e) {	}
		
	}
	public void getDeveloperOrderByEmply(){
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getDeveloperOrdeByEmployeesSQL);
		ResultSet rs = prstmt.executeQuery();
		System.out.print("  Name: 			Num. of Emply:: ");
		System.out.print("-------------------------------------------");
		while(rs.next()) {
			String name = rs.getString(3);
			int employees  = rs.getInt(4);
			System.out.print(name+"  		 "+employees);
		}
		
		}catch(SQLException e) {	}
		
	}
	
	public void deleteGameByName(String input) throws SQLException {
		PreparedStatement prstmt = this.conn.prepareStatement(DELETE_GAME_SQL);
		String name = input;
		prstmt.setString(2, name);
		prstmt.executeUpdate();
	}
	public void deleteDeveloperByName(String input) throws SQLException {
		PreparedStatement prstmt = this.conn.prepareStatement(DELETE_DEVELOPER_SQL);
		String name = input;
		prstmt.setString(3, name);
		prstmt.executeUpdate();
	}
	
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
	
	public ArrayList<String> getTableNames() throws SQLException{
		ArrayList<String> tableNames= new ArrayList<String>();
		DatabaseMetaData dmd = this.conn.getMetaData();
		ResultSet rs = dmd.getTables(null, "H21_YG5YAO", "%", null);
		while(rs.next()) {
			tableNames.add(rs.getString(3));
		}
		rs.close();
		return tableNames;
	}
	
	public int callableFibf(int number) throws SQLException {
		CallableStatement cs = this.conn.prepareCall(CALLABLE_TEST);
		cs.registerOutParameter(1, java.sql.Types.INTEGER);
		cs.setInt(2, number);
		cs.execute();
		int result = cs.getInt(1);
		cs.close();
		return result;
	}
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


	
}
