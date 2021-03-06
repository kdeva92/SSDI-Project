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

import antlr.debug.MessageListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;


public class Login {
	PasswordField password_text;
	TextField userName_text;
	public Presenter presenter;
	Button backBtn;
	Button loginBtn;
	
	public void loadLoginPage(Presenter present)
	{
		this.presenter = present;
VBox loginContainer = null;
try {
	loginContainer = (VBox) FXMLLoader.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/LoginWindow.fxml"));
	
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	
	loginBtn = (Button) loginContainer.lookup("#loginBtn");
	userName_text = (TextField) loginContainer.lookup("#userNameF");
	password_text = (PasswordField) loginContainer.lookup("#passwordF");
	backBtn = (Button) loginContainer.lookup("#backBtn");
	
	
	//Button sample = new Button("Sample");
	//loginPane.add(sample, 10,-10);
	
	Scene scene = new Scene(loginContainer,ChatApp.stage.getWidth(),ChatApp.stage.getHeight());
	scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());
	
	//ChatApp.stage.setMaximized(true);
	ChatApp.stage.setScene(scene);
	//ChatApp.stage.setMaximized(true);
	//ChatApp.stage.show();
	
	
	
	
	//Back Button Listener
	backBtn.setOnAction(new EventHandler<ActionEvent>() {
		
		public void handle(ActionEvent event) {
			presenter.loadHomepage();
		}
	});
	
	
	
	//Login Button Listeners
	
	userName_text.setOnKeyPressed(new EventHandler<KeyEvent>() {

		public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER){
				sendLoginMessage();
			}
			
		}
	});
	
	password_text.setOnKeyPressed(new EventHandler<KeyEvent>() {

		public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ENTER){
				sendLoginMessage();
			}
			
		}
	});
	
	
	loginBtn.setOnAction(new EventHandler<ActionEvent>() {

		public void handle(ActionEvent arg0) {
			//presenter.sendLoginMessage(userName_text.getText().trim(), password_text.getText().trim());
			sendLoginMessage();
			
		}
	});
	
	
	
	
	
	}
	void sendLoginMessage(){
		if(!userName_text.getText().trim().equals("") & !password_text.getText().trim().equals(""))
			presenter.sendLoginMessage(userName_text.getText().trim(), password_text.getText().trim());
		else
			Alerts.createInformationAlert("All fields must be completed", null, null);
		
	}
}
