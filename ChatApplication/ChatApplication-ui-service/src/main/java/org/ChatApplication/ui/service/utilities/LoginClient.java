/*
 * 
 * Author: Gaurav Dhamdhere
 * Last Modified on: 03/09/16
 * 
 * Description: Code for loading Login authentication
 * 
 */

package org.ChatApplication.ui.service.utilities;

public class LoginClient {

	public boolean authenticate(String username, String password)
	{
		
		if(username.equals("root") && password.equals("root"))
			return true;
		else
			return false;
	}
}
