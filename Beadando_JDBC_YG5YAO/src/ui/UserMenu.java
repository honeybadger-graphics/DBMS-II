package ui;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import database.Manager;

public class UserMenu {
	public static void main(String[] args) throws Exception {
		Menu();

	}
public static void Menu() throws Exception {
	Scanner keyboard = new Scanner(new InputStreamReader(
        System.in, Charset.forName("UTF-8")));
System.out.println("Do you want to enter name and password?(Y/N)");
//System.out.println("NOTE: If your PASSWORD CONTAINS special characters like #,&,{,} or else HIGHLY RECOMMEND to UNBIND these character's shortcut for the time-being or the console WONT RECOGNIZE it. ");
System.out.println("IF N is selected the program is going close and you need to manually set your password and username in the code marked with comments !!DO NOT CHANGE ANYWHERE ELSE!!");
String normalLogin = keyboard.nextLine();
switch (normalLogin) {  
case "Y":  
String user = enterUserName();
String pw = enterPassword();
    System.out.println("You entered: " + user); //same as below
    System.out.println("You entered: " + pw); //test purpose
    Manager currentManager = new Manager(user,pw);
    currentManager.Disconnect();
    break;
case "N":  
	String userManually = null; //YOU MAY CHANGE HERE FOR THE USERNAME format is: "username"
	String pwManually = null;  //YOU MAY CHANGE HERE FOR PASSWORD format is: "password"
	if(userManually != null && pwManually != null) 
	{
		System.out.println("You entered: " +userManually ); //same as in Y above
        System.out.println("You entered: " + pwManually); //sames as in Y above
        Manager currentManager2 = new Manager(userManually,pwManually);
        currentManager2.Disconnect();
        break;
	}
	else {System.out.println("You did not change the userManually and pwMaunally");
	break;}
}}
public static String enterUserName() 
{ Scanner keyboard = new Scanner(new InputStreamReader(
          System.in, Charset.forName("UTF-8")));
	 System.out.println("Enter username: ");
		String user = keyboard.nextLine();
		return user;
		}
public static String enterPassword() 
{ Scanner keyboard = new Scanner(new InputStreamReader(
         System.in, Charset.forName("UTF-8")));
	 System.out.println("Enter password: ");
		String pw = keyboard.nextLine();
		return pw;
		}
public void Operations(Manager currentManager) {
	/* this part will contain the switchers for the SQL operations and here they are going to run*/
	
}
}

