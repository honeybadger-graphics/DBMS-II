package models;

import java.sql.Date;

public class Game {
	private int id;
	private String name;
	private Date release;
	private Developer developer;
	
	
	public Game(int id, String name, Date release, Developer developer) {
		super();
		this.id = id;
		this.name = name;
		this.release = release;
		this.developer= developer;
	}

	public Developer getDeveloper() {
		return developer;
	}

	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
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
