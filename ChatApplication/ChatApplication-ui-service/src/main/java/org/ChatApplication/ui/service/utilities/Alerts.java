package org.ChatApplication.ui.service.utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Alerts {
	
	static void loginError()
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Login Error");
		alert.setHeaderText("Oops! Invalid EmailID / Password");
		alert.setContentText("Please check:\n1.If you have entered correct Email ID or Password\n2.Check if your CAPS Lock is ON");

		alert.showAndWait();
	}
	
	static void signUpSuccess()
	{
		Alert success = new Alert(AlertType.INFORMATION);
		success.setTitle("Registration Successful!");
		success.setHeaderText(null);
		success.setContentText("Please Login to Continue");

		success.showAndWait();
	}
	
	static void validations(int status)
	{
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Registration Error!!!");
		
		switch(status){
		
		case 2:
			error.setHeaderText("All Compulsory fields must be filled");
			error.setContentText("Compulsory Fields are ones markked by Asteriks (*)");
			break;
			
		case 3:
			error.setHeaderText("Invalid Email ID");
			error.setContentText("You can register only with UNCC Email ID.\n\nExample: abc@uncc.edu");
			break;
			
		case 4:
			error.setHeaderText("Invalid Contact");
			error.setContentText("Contact must be of xxx-xxxxxxx format. Example: 999-0000000");
			break;
		
		case 5:	
			error.setHeaderText("Paswords do not match");
			error.setContentText("Passwords entered in Password and Confirm Password fields must match");
			break;
			
		case 6:
			error.setHeaderText("Password does not satisfy the given requirements:");
			error.setContentText("1.Must contain one digit from 0-9\n2.Must contain one lowercase characters\n3.Must contain one uppercase characters\n4.Must contain one special symbol out of @ # $ %\n5.Length at least 8 characters and maximum of 20");
			break;
		}
		
		error.showAndWait();
	}

}
