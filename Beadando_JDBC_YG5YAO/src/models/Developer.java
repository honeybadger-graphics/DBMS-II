package models;

import java.sql.Date;

public class Developer {
	private final int id;
	private Date prodstart_date;
	private String name;
	private int employees ;
	
	public Developer(int id, Date prodstart_date, String name, int numberofemployees) {
		super();
		this.id = id;
		this.prodstart_date = prodstart_date;
		this.name = name;
		this.employees = numberofemployees;
	}

	public int getId() {
		return id;
	}

	public Date getProdStart_date() {
		return prodstart_date;
	}

	public void setProdStart_date(Date prodstart_date) {
		this.prodstart_date = prodstart_date;
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


	
}
