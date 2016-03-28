package org.ChatApplication.ui.service.utilities;

import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.models.User;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserRegisteration {

	TextField nameField;
	TextField emailField;
	TextField contactField;
	TextField ninerField;
	PasswordField passwordField;
	PasswordField confirmPasswordField;	
	
void loadSignupPage(){
	BorderPane signupPane = new BorderPane();

	HBox userInformationSection = new HBox();
	//userInformationSection.setPadding(new Insets(150,10,400,10));
	
	VBox labelSection = new VBox();
	labelSection.setPadding(new Insets(17,10,20,10));
	labelSection.setSpacing(13);
	
	VBox fieldSection = new VBox();
	fieldSection.setPadding(new Insets(10,10,10,10));
	fieldSection.setSpacing(5);
	
	
	Label nameLabel = new Label("Name:");
	Label emailLabel = new Label("Email ID:");
	Label ninerID = new Label("Niner ID:");
	Label contactLabel = new Label("Contact:");
	Label passwordLabel = new Label("Password:");
	Label confirmPasswordLabel = new Label("Confirm Password:");
	
	nameField = new TextField();
	emailField = new TextField();
	ninerField = new TextField();
	contactField = new TextField();
	passwordField = new PasswordField();
	confirmPasswordField = new PasswordField();
	
	HBox buttonSection = new HBox();
	buttonSection.setPadding(new Insets(17,10,20,10));
	buttonSection.setSpacing(60);
	Button registerButton = new Button("Register");
	registerButton.setPadding(new Insets(17,10,20,10));
	Button cancelButton = new Button("Cancel");
	cancelButton.setPadding(new Insets(17,10,20,10));
	buttonSection.getChildren().addAll(registerButton,cancelButton);
	
	labelSection.getChildren().addAll(nameLabel,ninerID,emailLabel,contactLabel,passwordLabel,confirmPasswordLabel);
	fieldSection.getChildren().addAll(nameField,ninerField,emailField,contactField,passwordField,confirmPasswordField);

	VBox registerSection = new VBox();
	registerSection.getChildren().addAll(userInformationSection,buttonSection);
	
	signupPane.setCenter(registerSection);
	userInformationSection.getChildren().addAll(labelSection,fieldSection);
	
	Scene scene = new Scene(signupPane,ChatApp.stage.getWidth(),ChatApp.stage.getHeight());
	scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());
	
	ChatApp.stage.setScene(scene);
	
	
	
	registerButton.setOnAction(new EventHandler<ActionEvent>() {
		
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			
			UserRegisterationClient urc = new UserRegisterationClient();
			int status = urc.validate(nameField.getText(),ninerField.getText(),emailField.getText(),contactField.getText(),passwordField.getText(),confirmPasswordField.getText());
		
			if(status == 1)	// Registration Success
			{
				User user = new User(ninerField.getText(),nameField.getText(),emailField.getText(),contactField.getText(),passwordField.getText());
				
				//boolean signUpStatus = komalSignupModule.signup()
				//if(signUpStatus)
				Alerts.signUpSuccess();	
				
				Login login = new Login();
				login.loadLoginPage();
				//else	-	Alert
			}
			else
			{
				Alerts.validations(status);
			}
		}
	});
	
	
	cancelButton.setOnAction(new EventHandler<ActionEvent>() {
		
		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			nameField.clear();
			emailField.clear();
			contactField.clear();
			passwordField.clear();
			confirmPasswordField.clear();
			
			Homepage homepage = new Homepage();
			homepage.loadHomepage();
		}
	});
	
	
	}
}
