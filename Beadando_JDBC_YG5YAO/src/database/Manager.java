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
import models.Developer; // still "car" from examples rename later

public class Manager {
	private final Connection conn;
	private static final String url="jdbc:oracle:thin:@193.6.5.58:1521:XE";
	private static final String classname="oracle.jdbc.driver.OracleDriver";
	
	private static final String insertGameSQL="insert into game values(?,?,?)";
	private static final String selectMaxGameIdSQL="select max(id) from game";
	private static final String getCarsByGameSQL="select * from car where game = ?";
	private static final String getGameByNameSQL="select * from game where name = ?";
	private static final String DELETE_GAME_SQL="delete from game where id = ?";
	private static final String SELECT_ALL_TABLES="Select * from game p left outer join car c on (p.id=c.game)";
	
	private static final String CALLABLE_TEST="{? = call fibf(?)}";
	
	
	public Manager(String username, String password) throws Exception {
		driverReg();
		this.conn = createConnection(username,password); 
		this.conn.setAutoCommit(false);
	}
	
	private void driverReg() throws ClassNotFoundException {
			Class.forName(classname);
	}
	
	private Connection createConnection(String username, String password) throws SQLException {	
		Connection conn;
			conn = DriverManager.getConnection(url,username,password);
			return conn;
	}
	
	public void insertGame(Game game) throws Exception {
		PreparedStatement prstmt = this.conn.prepareStatement(insertGameSQL);
		game.setId(getMaxGameId()+1);
		prstmt.setInt(1, game.getId());
		prstmt.setString(2, game.getName());
		prstmt.setDate(3, game.getBirth());
		
		if(prstmt.execute()) new Exception("Not successful");
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
	
	public ArrayList<Car> getCarsByGame(Game game) throws SQLException{
		ArrayList<Car> result = new ArrayList<Car>();
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getCarsByGameSQL);
		
		prstmt.setInt(1, game.getId());
		
		ResultSet rs = prstmt.executeQuery();
		
		while(rs.next()) {
			result.add(new Car(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4),game));
		}
		}catch (SQLException e) {
			return null;
		}
		return result;
	}
	
	public Game getGameByName(String name){
		try {
		PreparedStatement prstmt = this.conn.prepareStatement(getGameByNameSQL);
		prstmt.setString(1, name);
		ResultSet rs = prstmt.executeQuery();
		rs.next();
		return new Game(rs.getInt(1),rs.getString(2),rs.getDate(3));
		
		}catch(SQLException e) {	}
		
		return null;
	}
	
	public void deleteGameById(String input) throws SQLException {
		PreparedStatement prstmt = this.conn.prepareStatement(DELETE_GAME_SQL);
		int id = Integer.parseInt(input);
		prstmt.setInt(1, id);
		prstmt.executeUpdate();
	}
	
	public ArrayList<ArrayList<String>> selectGameAndCars() throws SQLException {
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
		ResultSet rs = dmd.getTables(null, "H20_GPNWZT", "%", null);
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
	
	public void commit() throws SQLException {
		this.conn.commit();
	}
	public void rollback() throws SQLException {
		this.conn.rollback();
	}

	
}
