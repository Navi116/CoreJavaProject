package com.navi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Provides admin controls.
 */
public class Admin {
	private static Connection conn;
	private static ResultSet rs;
	private static PreparedStatement ps;
	
	private static Scanner sc;
	private static String s;
	
	private static String adm_uname="admin";
	private static String adm_pass="admin123";
	
	
	/**
	 * Provides menu for admin and lists all the operations admin can perform after login.
	 * Admin details are directly stored in the variables instead of database.
	 * @throws SQLException because database errors may occur while connecting to database.
	 */
	public static void operations() throws SQLException {
		sc = new Scanner(System.in);
		
		System.out.println("Enter user name");
		String uname = sc.next();
		System.out.println("Enter password");
		String pass = sc.next();
		if(uname.equalsIgnoreCase(adm_uname) && pass.equals(adm_pass)) {
			while(true) {
				System.out.println("<=========Menu=========>");
				System.out.println("1. View Courses");
				System.out.println("2. Add Course");
				System.out.println("3. Show Students Enrolled");
				System.out.println("4. Add Student");
				System.out.println("<=========Menu=========>");
				System.out.println("Enter your choice");
				int choice = sc.nextInt();
				
				switch(choice) {
				case 1:
					existingcourses();
					break;
				case 2:
					// Adding new course
					addCourse();
					break;
				case 3:
					//Show Students enrolled and their details
					studentDetails();
					break;
				case 4:
					// To pay tution fee
					Operations.addStudent();
					break;
				default:
					System.out.println("Invalid Option try again.");
				}
				System.out.println("Do you want to continue or do you want to logout enter 'L' if you want to logout");
				char ch = sc.next().toLowerCase().charAt(0);
				if(ch=='l') {
					System.out.println("Logout successful.");
					break;
				}
			}//Closing menu loop(while)
			}else {
				System.out.println("Username or password is wrong");
			}
		
	}
	
		

	/**
	 * This method lists all the existing courses available in the course table.
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void existingcourses() throws SQLException {
		System.out.println("Existing Course List");
		conn = DbConnection.getConnection();
		s = "select * from course";
		ps = conn.prepareStatement(s);
		rs = ps.executeQuery();
		System.out.printf("%-10s%-25s%-10s%n","Course ID","Course Name","Course Fee");
		while(rs.next()) {
			System.out.printf("%-10d%-25s%-10.2f%n",rs.getInt(1),rs.getString(2),rs.getFloat(3));
		}
		
	}
	
	/**
	 * This method is used to add new courses to the course table.
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void addCourse() throws SQLException {
		System.out.println("Add course");
		sc = new Scanner(System.in);
		conn = DbConnection.getConnection();
		System.out.println("Enter new course ID");
		int cid = sc.nextInt();
		System.out.println("Enter the course name");
		sc.nextLine();
		String cname = sc.nextLine();
		System.out.println("Enter the course fee");
		float cfee = sc.nextFloat();
		
		s = "insert into course values(?,?,?)";
		ps = conn.prepareStatement(s);
		ps.setInt(1, cid);
		ps.setString(2, cname);
		ps.setFloat(3, cfee);
		int i = ps.executeUpdate();
		if(i>0) {
			System.out.println("Course added successfully");
		}else {
			System.out.println("Error occurred while adding new course");
		}
	}
	
	/**
	 * This method is used to show details of all the students enrolled for the course.
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	private static void studentDetails() throws SQLException {
		String s2;
		PreparedStatement ps2;
		ResultSet rs2;
		List<Integer> lst = new ArrayList<Integer>();
		s = "select * from student";
		conn = DbConnection.getConnection();
		ps = conn.prepareStatement(s);
		rs = ps.executeQuery();
		System.out.printf("%-15s%-20s%-20s%-10s%-20s%n","Student ID","Student Name","Student Email","Balance","Course(s) Enrolled");
		while(rs.next()) {
			int sid = rs.getInt(1);
			System.out.printf("%-15d%-20s%-20s%-10.2f",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getFloat(4));
			s2 = "select cid from enroll where sid=?";
			ps2 = conn.prepareStatement(s2);
			ps2.setInt(1, sid);
			rs2=ps2.executeQuery();
			while(rs2.next()) {
				lst.add(rs2.getInt("cid"));
			}
			if(lst.isEmpty()) {
				System.out.printf("%-10s%n","None");
			}else {
				for(int c:lst) {
					s2 = "select course_name from course where cid =?";
					ps2 = conn.prepareStatement(s2);
					ps2.setInt(1, c);
					rs2 = ps2.executeQuery();
					while(rs2.next()) {
						System.out.printf("%-20s",rs2.getString("course_name"));
					}System.out.println();
				}lst.clear();
			}
		}
	}


}
