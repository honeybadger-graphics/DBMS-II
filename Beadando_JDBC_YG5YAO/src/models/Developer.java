package models;

import java.sql.Date;

public class Developer {
	private static int id;
	private static Date start_date;
	private static String name;
	private static int employees ;
	
	public Developer(int id, Date start_date, String name, int numberofemployees) {
		super();
		Developer.id = id;
		Developer.start_date = start_date;
		Developer.name = name;
		Developer.employees = numberofemployees;
	}
 public String toString() 
 {
	return Developer.getStart_date()+" \t "+Developer.getName()+" \t "+Developer.getEmployees(); 
 }
	public static int getId() {
		return id;
	}

	public static Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date prodstart_date) {
		Developer.start_date = prodstart_date;
	}

	public static String getName() {
		return name;
	}

	public void setName(String name) {
		Developer.name = name;
	}

	public static int getEmployees() {
		return employees;
	}

	public void setEmployees(int numberofemployees) {
		Developer.employees = numberofemployees;
	}

	public void setId(int id) {
		Developer.id=id;
	}


	
}
