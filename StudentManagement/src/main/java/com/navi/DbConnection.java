package com.navi;

import java.sql.Connection;
import java.sql.DriverManager;
/**
 * Provides Database connection to the project.
 */
public class DbConnection {
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/studentdb";
	private static String uname = "root";
	private static String pass = "root";
	private static Connection conn;

	/**
	 * Is used for providing connection to the database/
	 * @return connection data.
	 */
	public static Connection getConnection() {
		
		try {
			//1. Load the driver
			Class.forName(driver);
			
			//2. Make connection
			conn = DriverManager.getConnection(url, uname, pass);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return conn;
		
	}

}
