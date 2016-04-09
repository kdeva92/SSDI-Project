package org.ChatApplication.ui.service.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.ChatApplication.data.entity.User;
import org.h2.tools.DeleteDbFiles;

public class ContactsHandler {

	
	public void add2Contacts(User user,Connection conn)
	{
		try {
			Statement st = conn.createStatement();
			st.execute("INSERT INTO User VALUES('"+user.getNinerId()+"','"+user.getFirstName()+" "+user.getLastName()+"','"+user.getEmail()+"','"+"9999999999"+"','"+user.getPassword()+"')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
