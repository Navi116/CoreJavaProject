package com.navi;

import java.sql.SQLException;
import java.util.Scanner;
/**
 * @author Naveen
 * @version 06/10/2023
 */
public class StudentManagement {
	/**
	 * This method displays the menu of operations user can select
	 * @param args some arguments for user input into main method
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void main(String[] args) throws SQLException {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("<=========Menu=========>");
			System.out.println("1. Add Student");
			System.out.println("2. Enroll a Course");
			System.out.println("3. View Balance");
			System.out.println("4. Pay Tution Fee");
			System.out.println("5. Show Student Status");
			System.out.println("6. Forgot Student ID");
			System.out.println("7. Admin Login");
			System.out.println("<=========Menu=========>");
			System.out.println("Enter your choice");
			int choice = sc.nextInt();
			
			switch(choice) {
			case 1:
				// To add new students we call add students method
				Operations.addStudent();
				break;
			case 2:
				// Enroll student to a specific course
				Operations.enrollCourse();
				break;
			case 3:
				//To view Balance
				Operations.viewBalance();
				break;
			case 4:
				// To pay tution fee
				Operations.payTutionFee();
				break;
			case 5:
				Operations.showStatus();
				break;
			case 6:
				Operations.getStudentID();
				break;
			case 7:
				Admin.operations();
				break;
			default:
				System.out.println("Invalid Option try again.");
			}
			System.out.println("Do you want to continue (y/n)");
			char c = sc.next().toLowerCase().charAt(0);
			if(c!='y') {
				break;
			}
		}
	}
}
