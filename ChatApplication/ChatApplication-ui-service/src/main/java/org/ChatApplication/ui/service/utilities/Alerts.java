package org.ChatApplication.ui.service.utilities;

import java.util.List;

import org.ChatApplication.ui.service.models.User;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class Alerts {

	static void loginError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Login Error");
		alert.setHeaderText("Oops! Invalid EmailID / Password");
		alert.setContentText(
				"Please check:\n1.If you have entered correct Email ID or Password\n2.Check if your CAPS Lock is ON");

		alert.showAndWait();
	}

	static void signUpSuccess() {
		Alert success = new Alert(AlertType.INFORMATION);
		success.setTitle("Registration Successful!");
		success.setHeaderText(null);
		success.setContentText("Please Login to Continue");

		success.showAndWait();
	}

	static void validations(int status) {
		Alert error = new Alert(AlertType.ERROR);
		error.setTitle("Registration Error!!!");

		switch (status) {
		
		
		case 2:
			error.setHeaderText("All Compulsory fields must be filled");
			error.setContentText("Compulsory Fields are ones markked by Asteriks (*)");
			break;
			
		case 3:
			error.setHeaderText("Niner ID Invalid");
			error.setContentText("Niner ID must be 9 digits starting with '800'");
			break;	

		case 4:
			error.setHeaderText("Invalid Email ID");
			error.setContentText("You can register only with UNCC Email ID.\n\nExample: abc@uncc.edu");
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

	static void createInformationAlert(String title, String header, String content) {
		Alert success = new Alert(AlertType.INFORMATION);
		success.setTitle(title);
		success.setHeaderText(null);
		success.setContentText(null);

		success.showAndWait();
	}

	static Alert createAdd2ContactAlert(List<User> userList) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("User Found");
		alert.setHeaderText("Do you want to add the User as your contact?");
		ButtonType buttonYes = new ButtonType("Yes");
		ButtonType buttonNo = new ButtonType("No");
		alert.getButtonTypes().setAll(buttonYes, buttonNo);
		// Optional<ButtonType> result = alert.showAndWait();
		// if (result.get() == buttonYes) {
		//
		// } else {
		// alert.close();
		// }

		return alert;

	}

	public static Alert createFileKeepAlert() {
		// TODO Auto-generated method stub
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("New incoming file");
		alert.setHeaderText("Do you want to keep this file?");
		
		
		return alert;
	}

}
