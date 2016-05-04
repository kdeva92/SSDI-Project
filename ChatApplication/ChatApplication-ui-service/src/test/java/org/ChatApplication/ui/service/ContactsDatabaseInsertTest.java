package org.ChatApplication.ui.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

public class ContactsDatabaseInsertTest extends TestCase {

	public void testDBInsert() {
		TestDatabaseConnecter testConnector = new TestDatabaseConnecter();
		Connection conn = testConnector.getConn();
		Statement stat;
		try {
			stat = conn.createStatement();
			System.out.println("Executed Queries\n");
			System.out.println(
					"CREATE TABLE IF NOT EXISTS Contacts_800934991(niner_id varchar(10),first_name varchar(60),last_name varchar(60),email varchar(60))\n");
			String id = "800934992";
			String email = "dkulkarn@uncc.edu";
			System.out.println(
					"INSERT INTO Contacts_800934991 VALUES('"+id+"','Devdatta','Kulkarni','"+email+" ')\n");
			stat.execute(
					"CREATE TABLE IF NOT EXISTS Contacts_800934991(niner_id varchar(10),first_name varchar(60),last_name varchar(60),email varchar(60))");

			stat.execute(
					"INSERT INTO Contacts_800934991 VALUES('"+id+"','Devdatta','Kulkarni','"+email+" ')");
			System.out.println("\n Select Query\n\n");
			ResultSet rs = stat.executeQuery("SELECT * from Contacts_800934991");
			rs.next();
			assertEquals(id, rs.getString(1));
			assertEquals(email, rs.getString(4));


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
