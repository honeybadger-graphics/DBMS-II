package ui;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import database.Manager;

public class UserMenu {
	public static void main(String[] args) throws Exception {
		Menu(); //calls the menu

	}
public static void Menu() throws Exception {
	Scanner keyboard = new Scanner(new InputStreamReader(
        System.in, Charset.forName("UTF-8")));
System.out.println("Do you want to enter name and password?(Y/N)");
//System.out.println("NOTE: If your PASSWORD CONTAINS special characters like #,&,{,} or else HIGHLY RECOMMEND to UNBIND these character's shortcut for the time-being or the console WONT RECOGNIZE it. ");
System.out.println("IF N is selected the program is going close and you need to manually set your password and username in the code marked with comments !!DO NOT CHANGE ANYWHERE ELSE!!");
String normalLogin = keyboard.nextLine(); //manual login switch best for testing purpose
switch (normalLogin) {  
case "Y":
case "y":
String user = enterUserName();
String pw = enterPassword();
    Manager currentManager = new Manager(user,pw);
    Commandlist();
    Operations(currentManager);
    currentManager.Disconnect();
    break;
case "N":
case "n": 
	String userManually = null; //YOU MAY CHANGE HERE FOR THE USERNAME format is: "username"
	String pwManually = null;  //YOU MAY CHANGE HERE FOR PASSWORD format is: "password"
	if(userManually != null && pwManually != null) 
	{
        Manager currentManager2 = new Manager(userManually,pwManually);
        Commandlist();
        Operations(currentManager2);
        currentManager2.Disconnect();
        break;
	}
	else {System.out.println("You did not change the userManually and pwMaunally");
	break;}
}}
private static void Commandlist() {
	String[] commands = {"Delete tables", "Create tables"
	        ,"Filling tables with data","Change style"  
	        ,"Change school name", "Delete data", "Data from user", "Get table datas", "Hip-hop teachers"  
	        ,"Capacity decrease","Select capacity","Average capacity","Change date"  
	        ,"Select Teacher name"}; // change names to represent its commands
	for(int i=0; i < commands.length;i++) {System.out.println(i+1 + "." + commands[i]);  
}
}
//user input for username
public static String enterUserName() 
{ Scanner keyboard = new Scanner(new InputStreamReader(
          System.in, Charset.forName("UTF-8")));
	 System.out.println("Enter username: ");
		String user = keyboard.nextLine();
		return user;
		}
//user input for pw
public static String enterPassword() 
{ Scanner keyboard = new Scanner(new InputStreamReader(
         System.in, Charset.forName("UTF-8")));
	 System.out.println("Enter password: ");
		String pw = keyboard.nextLine();
		return pw;
		}
public static void Operations(Manager currentManager) {
	/* this part will contain the switchers for the SQL operations and here they are going to run*/
	int exiter; // user input for exiting the menu and disconnects from db if not calls itself to run other operations
	Scanner operations = new Scanner(new InputStreamReader(
	          System.in, Charset.forName("UTF-8")));
			int menubuttons = operations.nextInt();
	switch (menubuttons) {  
	case 1:
		currentManager.CreateTables();
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{
			Operations(currentManager);}
	case 2:
		currentManager.InsertVaules();
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 3:
		// insert into developer
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{
			Operations(currentManager);}
	case 4:
		// insert into game
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 5:
		// update gamegenre where name
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 6:
		// update deveempl where name
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 7:
		// getGamesByDeveloperSQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 8:
		// getGameOrdeByNameSQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 9:
		// getDeveloperOrdeByEmployeesSQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 10:
		// getDeveloperByGameSQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 11:
		// DELETE_GAME_SQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 12:
		// DELETE_DEVELOPER_SQL
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 13:
		// SELECT_ALL_TABLES
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
	case 14:
		// File kiiaratas
		exiter = operations.nextInt();
		if(exiter == 1) 
		{break;} 
		else 
		{Operations(currentManager);}
}
}
}

