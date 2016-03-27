/*
 * 
 * Author: Gaurav Dhamdhere
 * Last Modified on: 03/09/16
 * 
 * Description: Code for loading Login Page
 * 
 */

package org.ChatApplication.ui.service.utilities;



import org.ChatApplication.ui.service.application.ChatApp;




import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
	GridPane loginPane = new GridPane();
	
	loginPane.setAlignment(Pos.CENTER);
	loginPane.setHgap(10);
	loginPane.setVgap(10);
	loginPane.setPadding(new Insets(25,25,25,25));
	
	Label loginHeading = new Label("UNCC Chat User Login");
	loginHeading.setStyle("-fx-font: 50 arial");
	
	
	Label userName = new Label("Username:");
	userName.setStyle("-fx-font: 16 arial");
	userName.setPadding(new Insets(5,0,0,0));
	
	userName_text = new TextField();
	userName_text.setPrefSize(300, 30);
	
	
	Label password = new Label("Password:");
	password.setStyle("-fx-font: 16 arial");

	password_text = new PasswordField();
	password_text.setPrefSize(300, 30);
	
	Button loginBtn = new Button("Login");
	loginBtn.setPrefSize(200,50);
	loginBtn.setStyle("-fx-font: 18 arial; -fx-base:  #b0e0e6");
	
	Button backBtn = new Button("Back to Homepage");
	backBtn.setPrefSize(200,50);
	backBtn.setStyle("-fx-font: 18 arial; -fx-base:  #b0e0e6");
	
	
	
	VBox container = new VBox();
	container.setSpacing(100);
	container.setAlignment(Pos.CENTER);
	
	VBox heading = new VBox();
	heading.getChildren().add(loginHeading);
	
	HBox credentials = new HBox();
	credentials.setSpacing(20);
	credentials.setAlignment(Pos.CENTER);
	
	VBox labels = new VBox();
	labels.setSpacing(20);
	labels.getChildren().addAll(userName,password);
	
	VBox fields = new VBox();
	fields.setSpacing(10);
	fields.getChildren().addAll(userName_text,password_text);
	
	credentials.getChildren().addAll(labels,fields);
	
	HBox buttonBox = new HBox();
	buttonBox.setSpacing(10);
	buttonBox.setAlignment(Pos.CENTER);
	buttonBox.getChildren().addAll(loginBtn,backBtn);
	
	VBox operatorPart = new VBox();
	operatorPart.setSpacing(20);
	operatorPart.setAlignment(Pos.CENTER);
	operatorPart.getChildren().addAll(credentials,buttonBox);
	
	container.getChildren().addAll(heading,operatorPart);
	
	loginPane.add(container, 0, 0);
	
	
	
	//Button sample = new Button("Sample");
	//loginPane.add(sample, 10,-10);
	
	Scene scene = new Scene(loginPane,ChatApp.stage.getWidth(),ChatApp.stage.getHeight());
	scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());
	
	//ChatApp.stage.setMaximized(true);
	ChatApp.stage.setScene(scene);
	//ChatApp.stage.setMaximized(true);
	//ChatApp.stage.show();
	
	
	
	
	//Back Button Listener
	backBtn.setOnAction(new EventHandler<ActionEvent>() {
		
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			Homepage homepage = new Homepage();
			homepage.loadHomepage();
		}
	});
	
	
	
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
