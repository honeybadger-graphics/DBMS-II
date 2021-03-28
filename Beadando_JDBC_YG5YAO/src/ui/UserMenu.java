package ui;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import database.Manager;
import models.Developer;
import models.Game;

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
	String[] commands = {"Create tables",
	        "Filling tables with data","Add new developer"  
	        ,"Add new game", "Change genre of a game", "Change number of employees of a developer", "Get games from a developer", "Developer of a game"  
	        ,"Developers ordered by number of employees","Games ordered by name","Delete from game","Delete from developer","Selects everything from game and developer","All records from tabels into .TXT file ",
	        "Write Metadata" , "Developers with less the X numberber of employees"
	        };
	for(int i=0; i < commands.length;i++) {System.out.println(i+1 + ". " + commands[i]);  
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
	Scanner operations = new Scanner(new InputStreamReader(
	          System.in, Charset.forName("UTF-8")));
	 System.out.println("Number of operations to run:");
			int numberofoperations = operations.nextInt();
			int[] menubuttons = new int[numberofoperations];
			for(int i=0; i<menubuttons.length;i++) {System.out.println(i+1 +". operation is:");
			menubuttons[i] = operations.nextInt();
			}
			for(int i=0; i<menubuttons.length;i++) {switch (menubuttons[i]) {  
			case 1: //Works as intended
				currentManager.CreateTables();
				break; 
			case 2: //works as intended
				currentManager.InsertVaules();
				break;
			case 3: //works as intended
				// insert into developer
				Scanner inputs = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the developer:");
				String dname = inputs.nextLine();
				System.out.println("Enter creation date of the developer: (Format: yyyy-mm-dd)" );
				String ddate = inputs.nextLine();
				System.out.println("Enter the number of employees of the developer:");
				int demply = inputs.nextInt();
				Date Fddate= Date.valueOf(ddate);
				Developer newDeveloper = new Developer(0,Fddate,dname,demply);
				try {
					currentManager.insertDeveloper(newDeveloper);
					currentManager.commit();
				} catch (Exception e) {
					System.err.println("Cannot insert new developer. "+ e.getMessage());  
				}
				break;
			case 4: //works as intended
				// insert into game
				Scanner input = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the game:");
				String name = input.nextLine();
				System.out.println("Enter the genre of the game:");
				String genre = input.nextLine();
				System.out.println("Enter release date of the game: (Format: yyyy-mm-dd)");
				String date = input.nextLine();
				System.out.println("Enter the ID fo the developer:");
				int devid = input.nextInt();
				Date Fdate= Date.valueOf(date);
				Game newGame = new Game(0,name,genre,Fdate,devid);
				try {
					currentManager.insertGame(newGame);
					currentManager.commit();
				} catch (Exception e) {
					System.err.println("Cannot insert new game. "+ e.getMessage());  
				}
				
				break;
			case 5:  //works as intended
				// update gamegenre where name
				Scanner input4 = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the Game to be updated:");
				String inputgamename = input4.nextLine();
				System.out.println("Enter the new genre:");
				String inputgamegenre = input4.nextLine();
				currentManager.updateGameGenre(inputgamename, inputgamegenre);

				break;
			case 6: //works as intended
				// update deveempl where name
				Scanner input3 = new Scanner(new InputStreamReader(System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the Developer to be updated:");
				String inputdevname = input3.nextLine();
				System.out.println("Enter the new number of employees:");
				int inputdevemployees = input3.nextInt();
				currentManager.updateDeveloperEmployees(inputdevname,inputdevemployees);
				break;
			case 7: //works as intended
				// getGamesByDeveloperSQL
				Scanner input6 = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the developer:");
				String devname = input6.nextLine();
				try {
					ArrayList<Game> Games = currentManager.getGamesByDeveloper(devname);
					System.out.println("Games by : "+devname);
					System.out.println("--------------------------------------------------------------");
					currentManager.writeList(Games);
				} catch (SQLException e) {
					System.err.println("Something went wrong!");
					e.printStackTrace();
				}
				break;
			case 8: //works as intended
				// getDeveloperByGameSQL
				Scanner input2 = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the game:");
				String gameID = input2.nextLine();
				Developer gamedeveloper = currentManager.getDeveloperByGame(gameID);
				System.out.println("CreationDate: \t Name: \t Num. of Emply:");
				System.out.println("--------------------------------------------------------------");
				System.out.println(gamedeveloper);
				break;
			case 9: //works as intended
				// getDeveloperOrdeByEmployeesSQL
				currentManager.getDeveloperOrderByEmply();
				break;
			case 10: //works as intended
				// getGameOrdeByNameSQL
				currentManager.getGameOrderByName();
				break;
			case 11: //works as intended
				// DELETE_GAME_SQL
				Scanner inputforgamedeletion = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the game to de deleted:");
				String nametobedeletedgame = inputforgamedeletion.nextLine();
					currentManager.deleteGameByName(nametobedeletedgame);
				break;
			case 12: //works as intended
				// DELETE_DEVELOPER_SQL
				Scanner inputfordevdeletion = new Scanner(new InputStreamReader(
				          System.in, Charset.forName("UTF-8")));
				System.out.println("Enter the name of the developer to de deleted:");
				String nametobedeleteddev = inputfordevdeletion.nextLine();
					currentManager.deleteDeveloperByName(nametobedeleteddev);
					break;
			case 13: //works as intended
				// SELECT_ALL_TABLES
				try {
					ArrayList<ArrayList<String>> result = currentManager.selectGameAndDevelopers();
					System.out.println("--------------------------------------------------------------");
					currentManager.writeArryArrayList(result);
				} catch (SQLException e) {
					System.err.println("Something went wrong!");
					e.printStackTrace();
				}
				break;
			case 14: //works as intended
				// File kiiaratas
				currentManager.WriteIntoFile();
				break;
			case 15: // works as intended
				try {
					ArrayList<String> result=currentManager.getTableNames();
					currentManager.writeArrayList(result);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case 16: //works as intended
				// kurzorkezelés with employees less than X
				currentManager.Employees();
				break;
		}}
	
}

}

