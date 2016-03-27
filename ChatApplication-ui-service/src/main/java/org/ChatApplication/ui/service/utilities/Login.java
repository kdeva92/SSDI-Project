/*
 * 
 * Author: Gaurav Dhamdhere
 * Last Modified on: 03/09/16
 * 
 * Description: Code for loading Login Page
 * 
 */

package org.ChatApplication.ui.service.utilities;



import java.io.IOException;

import org.ChatApplication.ui.service.application.ChatApp;




import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Login {
	PasswordField password_text;
	TextField userName_text;
	
	
	
	public void loadLoginPage()
	{
		
VBox loginContainer = null;
try {
	loginContainer = (VBox) FXMLLoader.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/LoginWindow.fxml"));
	
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
	Button loginBtn = (Button) loginContainer.lookup("#loginBtn");
	userName_text = (TextField) loginContainer.lookup("#userNameF");
	password_text = (PasswordField) loginContainer.lookup("#passwordF");
	
	//Button sample = new Button("Sample");
	//loginPane.add(sample, 10,-10);
	
	Scene scene = new Scene(loginContainer,ChatApp.stage.getWidth(),ChatApp.stage.getHeight());
	scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());
	
	//ChatApp.stage.setMaximized(true);
	ChatApp.stage.setScene(scene);
	//ChatApp.stage.setMaximized(true);
	//ChatApp.stage.show();
	
	
	
	
	//Back Button Listener
//	backBtn.setOnAction(new EventHandler<ActionEvent>() {
//		
//		public void handle(ActionEvent event) {
//			// TODO Auto-generated method stub
//			Homepage homepage = new Homepage();
//			homepage.loadHomepage();
//		}
//	});
	
	
	
	//Login Button Listener
	
	loginBtn.setOnAction(new EventHandler<ActionEvent>() {

		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			LoginClient loginClient = new LoginClient();
			
			
			if(loginClient.authenticate(userName_text.getText(),password_text.getText() ))
				{
				ChatPage chatPage = new ChatPage();
				chatPage.loadChatPage(userName_text.getText());
				}
			else
				{
				userName_text.clear();
				password_text.clear();
				
				Alerts.loginError();
				}
			//System.out.println("Login Status = "+status);
		}
	});
	
	
	
	}
}
