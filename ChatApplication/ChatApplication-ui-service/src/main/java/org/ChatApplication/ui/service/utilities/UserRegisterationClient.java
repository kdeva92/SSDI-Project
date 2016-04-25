package org.ChatApplication.ui.service.utilities;

import java.util.regex.Pattern;

import org.ChatApplication.data.entity.UserVO;

public class UserRegisterationClient {
	
	public int validate(UserVO user, String password2)
	{
		// Validate Compulsory Fields
		if(user.getFirstName().isEmpty() || user.getLastName().isEmpty() || user.getEmail().isEmpty() || password2.isEmpty() || user.getPassword().isEmpty() || user.getNinerId().isEmpty())
			return 2;
		
		// Validate Email ID
		if(!user.getEmail().endsWith("@uncc.edu"))
			return 3;
		
		
		// Validate passwords
		if(!user.getPassword().equals(password2))
			return 4;
		
		Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})");
		if(!pattern.matcher(user.getPassword()).matches())
			return 5;
		
		return 1;
		
	}
	
	

}
