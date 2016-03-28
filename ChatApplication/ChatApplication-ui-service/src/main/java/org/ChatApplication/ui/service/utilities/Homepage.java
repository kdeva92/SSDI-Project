/*
 * 
 * Author: Gaurav Dhamdhere
 * Last Modified on: 03/08/16
 * 
 * Description: Code for loading Homepage of the Chat Application
 * 
 */

package org.ChatApplication.ui.service.utilities;


import java.io.IOException;

import org.ChatApplication.ui.service.application.ChatApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;



public class Homepage{

	public void loadHomepage()
	{
		Thread msgReciever = new Thread();
		msgReciever.start();
		
		GridPane homePane = new GridPane();
		
		Button loginBtn = new Button("Login");
		Button signupBtn = new Button("Sign Up");
		
		Label heading = new Label("Welcome to UNCC Chat Application");
		heading.setStyle("-fx-font: 50 arial; -fx-text-fill: #ffffff");
		heading .setAlignment(Pos.TOP_CENTER);
		
		VBox container = new VBox();
		container.setSpacing(100);
		container.setAlignment(Pos.CENTER);
		VBox headingBox = new VBox();
		headingBox.setAlignment(Pos.CENTER);
		VBox buttonBox = new VBox();
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setSpacing(10);
		container.getChildren().addAll(headingBox,buttonBox);
		headingBox.getChildren().add(heading);
		buttonBox.getChildren().addAll(loginBtn,signupBtn);
		
		homePane.setAlignment(Pos.CENTER);
		homePane.setHgap(10);
		homePane.setVgap(10);
		homePane.setPadding(new Insets(25,25,25,25));
		
		
		
		
		homePane.add(container,0, 0);
		
		
		
		loginBtn.setPrefSize(200,50);
		signupBtn.setPrefSize(200,50);
		loginBtn.setStyle("-fx-font: 22 arial; -fx-base:  #b0e0e6");
		signupBtn.setStyle("-fx-font: 22 arial; -fx-base:  #b0e0e6");
		
		Scene scene = new Scene(homePane,ChatApp.stage.getWidth(),ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());
		
		ChatApp.stage.setScene(scene);
		
		
		
		
		
		loginBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
//				Login login = new Login();
//				login.loadLoginPage();
				ChatPage chat = new ChatPage();
				try {
					chat.loadChatPage("Test_User");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		signupBtn.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				UserRegisteration userRegister = new UserRegisteration();
				userRegister.loadSignupPage();
			}
		});
		
		
		
		
		
	}

	
	


}
