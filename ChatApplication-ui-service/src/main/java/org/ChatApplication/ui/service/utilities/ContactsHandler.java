package org.ChatApplication.ui.service.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.DeleteDbFiles;

public class ContactsHandler {

	
	
	void searchUser(String user)
	{
		try {
			Class.forName("org.h2.Driver");
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		// Komal Method call
		
		boolean found = true;
		
		if (found)
		{
			//DeleteDbFiles.execute("/org/ChatApplication/ui/service/database", "ChatClient", true);
			try {
				Connection conn = DriverManager.getConnection("jdbc:h2:./src/main/java/org/ChatApplication/ui/service/database/ChatClient");
				Statement stat = conn.createStatement();
				
				stat.execute("CREATE TABLE User(ninerId varchar(10) primary key,studentName varchar(60))");
				stat.execute("INSERT INTO User VALUES('800934991','"+user+"')");
				
				ResultSet rs;
				 rs = stat.executeQuery("SELECT * FROM USER");
				 while(rs.next())
				 {
					 System.out.println(rs.getString(1)+","+rs.getString(2));
				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
