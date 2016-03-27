package org.ChatApplication.ui.service.utilities;

import java.util.regex.Pattern;

public class UserRegisterationClient {
	
	public int validate(String name, String ninerID, String email, String contact, String password1, String password2)
	{
		// Validate Compulsory Fields
		if(name.isEmpty() || email.isEmpty() || password1.isEmpty() || password2.isEmpty() || ninerID.isEmpty())
			return 2;
		
		// Validate Email ID
		if(!email.endsWith("@uncc.edu"))
			return 3;
		
		// Validate Contact
		Pattern pattern = Pattern.compile("\\d{3}-\\d{7}");
		if(!contact.isEmpty() && !pattern.matcher(contact).matches())
			return 4;
		
		// Validate passwords
		if(!password1.equals(password2))
			return 5;
		
		pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})");
		if(!pattern.matcher(password1).matches())
			return 6;
		
		return 1;
		
	}

}
