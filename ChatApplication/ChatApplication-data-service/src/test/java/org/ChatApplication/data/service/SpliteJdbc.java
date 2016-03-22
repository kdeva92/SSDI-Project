package org.ChatApplication.data.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.ChatApplication.data.entity.User;

import junit.framework.TestCase;

public class SpliteJdbc extends TestCase {

	public SpliteJdbc() {
		// TODO Auto-generated constructor stub
	}

	public void testApp() {

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE COMPANY " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL, " + " AGE            INT     NOT NULL, "
					+ " ADDRESS        CHAR(50), " + " SALARY         REAL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			assertTrue(false);
			System.exit(0);
		}
		assertTrue("Table created successfully", true);

	}

	public void createUser() {
		User user = new User();
		user.setEmail("kigale@uncc.edu");
		user.setFirstName("Komal");
		user.setLastName("Ingale");
		user.setPassword("demo123");

		try {
			UserService.getInstance().createUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);
	}
}
