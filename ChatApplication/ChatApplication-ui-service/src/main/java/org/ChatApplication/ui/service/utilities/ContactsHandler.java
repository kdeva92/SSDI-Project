package org.ChatApplication.ui.service.utilities;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.ChatApplication.data.entity.UserVO;

public class ContactsHandler {

	
	public void add2Contacts(UserVO user,Connection conn)
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
