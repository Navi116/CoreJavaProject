package com.navi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class implements all of the operations.
 */
public class Operations {
	private static Scanner sc;
	private static Connection conn;
	private static ResultSet rs;
	private static PreparedStatement ps;
	private static String s,sname,semail;
	private static int sid,cid,i;
	private static Float amount,bal ;
	private static List<Integer> courseId;
	
	
	/**
	 * This method is used to add new students.
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void addStudent() throws SQLException {
		
		System.out.println("ADD STUDENT");
		//Initialize scanner class to take inputs from user
		sc = new Scanner(System.in);
		
		System.out.println("Enter Student name");
		sname = sc.nextLine();
		
		System.out.println("Enter Student email");
		semail = sc.next();
		
		s= "insert into student(sname,semail,balance) values(?,?,0)";
		conn = DbConnection.getConnection();
		
		ps = conn.prepareStatement(s);
		ps.setString(1, sname);
		ps.setString(2, semail);
		
		i = ps.executeUpdate();
		if(i>0) {
			System.out.println("Records added Successfully");
			//If records are added successfully we are displaying all the inserted records
			//This is to ensure that correct records are entered and to give the student their student Id.
			s = "select sid,sname,semail from student where semail = ?";
			ps = conn.prepareStatement(s);
			ps.setString(1, semail);
			rs = ps.executeQuery();
			System.out.printf("%-15s%-20s%-20s%n","Student ID","Student Name","Student Email");
			while(rs.next()) {
				System.out.printf("%-15d%-20s%-20s%n",rs.getInt("sid"),rs.getString("sname"),rs.getString("semail"));
			}
		}else {
			System.out.println("An error occurred while entering records");
		}	
	}
	/**
	 * This method is to get student ID
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void getStudentID() throws SQLException {
		System.out.println("Forgot Student ID");
		conn = DbConnection.getConnection();
		
		sc = new Scanner(System.in);
		System.out.println("Enter Student Mail");
		semail = sc.next();
		try {
			s = "select sid from student where semail=?";
			ps = conn.prepareStatement(s);
			ps.setString(1, semail);
			rs = ps.executeQuery();
			if(rs.next()) {
				System.out.println(rs.getInt("sid"));
			}
		}catch(Exception e) {
			System.out.println("Student already exists with given mail.");
		}
		
		
	}
	/**
	 * This method is for checking if the sid exists or not in the student table
	 * @param sid represents student id
	 * @return is used to return if student id exists or not
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static boolean studentId(int sid) throws SQLException {
		conn = DbConnection.getConnection();
		s="select sid from student where sid=?";
		ps = conn.prepareStatement(s);
		ps.setInt(1, sid);
		rs = ps.executeQuery();
		if(rs.next()) {
			return true;
		}else {
			return false;
		}
		
	}
	
	/**
	 * This method allows student to enroll into a course
	 * @throws SQLException because database errors may occur while connecting to database.
	 */
	public static void enrollCourse() throws SQLException {
		System.out.println("Enroll course");
		conn = DbConnection.getConnection();
		float cfee;
		
		
		//Initializing courseId array list to store all the course id's in the list.
		courseId = new ArrayList<Integer>();
		sc = new Scanner(System.in);
		
		//Printing course List
		s ="select * from course";
		ps = conn.prepareStatement(s);
		rs = ps.executeQuery();
		System.out.printf("%-10s%-25s%-10s%n%n","Course ID","Course Name","Course Fee");
		while(rs.next()) {
			System.out.printf("%-10d%-25s%-10.2f%n",rs.getInt(1),rs.getString(2),rs.getFloat(3));
			courseId.add(rs.getInt("cid"));
		}
		
		
		while(true) {
			System.out.println("Enter your student id");
			sid = sc.nextInt();
			if(studentId(sid)==true) {
				break;
			}else {
				System.out.println("Student ID is incorrect. Please enter correct student ID.");
			}
		}
		System.out.println("Enter a course ID which you want to enroll.....(from the abouve list)");
		cid = sc.nextInt();
		
		if(courseId.contains(cid)) {
			
			/*Checking if the student is already registered for the same course
			 * If yes, then we will print "You have already registered for this course
			 * If no, then we will allow the student to register for the course
			 */
			List<Integer> lst = new ArrayList<Integer>();
			s="select cid from enroll where sid=?";
			ps = conn.prepareStatement(s);
			ps.setInt(1, sid);
			rs = ps.executeQuery();
			while(rs.next()) {
				lst.add(rs.getInt("cid"));
			}
			if(lst.contains(cid)) {
				System.out.println("You have already registered for this course.");
			}else {
				s = "select cfee from course where cid=?";
				ps = conn.prepareStatement(s);
				ps.setInt(1, cid);
				rs = ps.executeQuery();
				if(rs.next()) {
					cfee = rs.getFloat("cfee");
					
					while(true) {
						System.out.println("Enter the amount you want to pay.");
						amount = sc.nextFloat();
						if(amount>=0) {
							break;
						}else {
							System.out.println("Invalid amount. Enter amount not less than 0");
						}
					}
					amount = cfee-amount;
					
					// Get existing balance
					s= "select balance from student where sid=?";
					ps = conn.prepareStatement(s);
					ps.setInt(1, sid);
					rs = ps.executeQuery();
					if(rs.next()) {
						bal = rs.getFloat("balance");
					}
					amount +=bal;
					
					//Updating Balance
					s = "update student set balance =? where sid =?";
					ps = conn.prepareStatement(s);
					ps.setFloat(1, amount);
					ps.setInt(2, sid);
					i = ps.executeUpdate();
					if(i<=0) {
						System.out.println("Error while updating course fee");
					}
				
				 
				// After Updating the balance inserting student and course details in enroll table 
				s = "insert into enroll(sid,cid) values(?,?)";
				ps = conn.prepareStatement(s);
				ps.setInt(1, sid);
				ps.setInt(2, cid);
				i =ps.executeUpdate();
				if(i>0) {
					System.out.println("Enrollment successfull");
				}else {
					System.out.println("Error occured while enrolling course");
				}
				}
			}
				
		}else {
			System.out.println("Course ID doesn't match.");
		}
		
	}
	
	/**
	 * This method is to check the balance, so the student knows the pending balance
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void viewBalance() throws SQLException {
		System.out.println("View Balance");
		conn = DbConnection.getConnection();
		
		sc = new Scanner(System.in);
		// Validating whether the student ID exists or not.
		while(true) {
			System.out.println("Enter your student id");
			sid = sc.nextInt();
			if(studentId(sid)==true) {
				break;
			}else {
				System.out.println("Student ID is incorrect. Please enter correct student ID.");
			}
		}
		//For getting balance
		s = "select balance from student where sid=?";
		ps = conn.prepareStatement(s);
		ps.setInt(1, sid);
		rs = ps.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getFloat("balance"));
		}
		
	}
	
	/**
	 * This method allows student to pay tution fee
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void payTutionFee() throws SQLException {
		sc = new Scanner(System.in);
		
		conn = DbConnection.getConnection();
		
		// Validating whether the student ID exists or not.
		while(true) {
			System.out.println("Enter your student id");
			sid = sc.nextInt();
			if(studentId(sid)==true) {
				break;
			}else {
				System.out.println("Student ID is incorrect. Please enter correct student ID.");
			}
		}

		s = "select balance from student where sid=?";
		ps = conn.prepareStatement(s);
		ps.setInt(1, sid);
		rs = ps.executeQuery();
		if(rs.next()) {
			bal = rs.getFloat("balance");
		}
		System.out.println("Your Balance is "+bal);
		if(bal==0) {
			System.out.println("You have no pending fee.");
		}else {
			while(true) {
				System.out.println("Enter the amount to pay");
				amount = sc.nextFloat();
				if(amount>=0) {
					break;
				}else {
					System.out.println("Invalid amount. Enter amount not less than 0");
				}
			}
			amount = bal-amount;
			s = "update student set balance=? where sid=?";
			ps = conn.prepareStatement(s);
			ps.setFloat(1, amount);
			ps.setInt(2, sid);
			i = ps.executeUpdate();
			if(i>0) {
				System.out.println("Tution fee paid.");
			}
		}
		
	}
	
	/**
	 * This method shows the  student status(like student details,courses enrolled,balance....)
	 * @throws SQLException because database errors may occur while connecting to database
	 */
	public static void showStatus() throws SQLException {
		sc = new Scanner(System.in);
		courseId = new ArrayList<Integer>();
		System.out.println("Enter Student ID");
		sid = sc.nextInt();
		//Get connection
		conn = DbConnection.getConnection();
		
		//Get courses enrolled by student
		s= "select cid from enroll where sid = ?";
		ps = conn.prepareStatement(s);
		ps.setInt(1, sid);
		rs = ps.executeQuery();
		while(rs.next()) {
			courseId.add(rs.getInt("cid"));
		}
		
		
		// Preparing statement
		s = "select * from student where sid =?";
		ps = conn.prepareStatement(s);
		ps.setInt(1, sid);
		rs = ps.executeQuery();
		while(rs.next()) {
			System.out.printf("%-15s : %-10d%n%-15s : %-20s%n%-15s : %-30s%n%-15s : %-10.2f%n"
					,"Student ID",rs.getInt(1),"Student Name",rs.getString(2),"Student Email",rs.getString(3),"Balance",rs.getFloat(4));
			System.out.printf("%s :","Courses Enrolled");
			for(int course: courseId) {
				s = "select course_name from course where cid=?";
				ps =conn.prepareStatement(s);
				ps.setInt(1, course);
				rs = ps.executeQuery();
				while(rs.next()) {
					System.out.printf("%-20s%n",rs.getString("course_name"));
				}
				
			}
		}
	}
	


}
