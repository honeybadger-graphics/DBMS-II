package models;

import java.sql.Date;

public class Game {
	private int id;
	private String name;
	private String genre;
	private Date release;
	private int developerID;
	
	
	public Game(int id, String name, String genre, Date release, int developerID) {
		super();
		this.id = id;
		this.name = name;
		this.genre = genre;
		this.release = release;
		this.developerID= developerID;
	}
	public String toString() 
	 {
		return name+" \t "+genre+" \t "+release; 
	 }

	public int getDeveloperID() {
		return developerID;
	}

	public void setDeveloper(int developerID) {
		this.developerID = developerID;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public Date getRelease() {
		return release;
	}


	public void setRelease(Date release) {
		this.release = release;
	}


	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id=id;
	}
	
	
}
