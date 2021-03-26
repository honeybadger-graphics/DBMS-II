package models;

import java.sql.Date;

public class Developer {
	private int id;
	private Date start_date;
	private String name;
	private int employees ;
	
	public Developer(int id, Date start_date, String name, int numberofemployees) {
		super();
		this.id = id;
		this.start_date = start_date;
		this.name = name;
		this.employees = numberofemployees;
	}

	public int getId() {
		return id;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date prodstart_date) {
		this.start_date = prodstart_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getEmployees() {
		return employees;
	}

	public void setEmployees(int numberofemployees) {
		this.employees = numberofemployees;
	}

	public void setId(int id) {
		this.id=id;
	}


	
}
