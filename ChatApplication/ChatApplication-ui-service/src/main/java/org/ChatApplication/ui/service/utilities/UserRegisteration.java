package org.ChatApplication.ui.service.utilities;

import java.io.IOException;

import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.ui.service.application.ChatApp;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserRegisteration {

	Button signUpBtn;
	Button cancelBtn;
	Presenter presenter;
	TextField idField;
	TextField firstNameField;
	TextField emailField;
	TextField lastNameField;
	PasswordField passwd1Field;
	PasswordField passwd2Field;

	void loadSignupPage(Presenter present) throws IOException {
		this.presenter = present;

		VBox signUpBox = (VBox) FXMLLoader
				.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/SignUpWindow.fxml"));
		HBox infoEntryBox = (HBox) signUpBox.lookup("#infoEntryBox");

		VBox fieldsBox = (VBox) infoEntryBox.lookup("#fieldsBox");
		VBox instructionBox = (VBox) infoEntryBox.lookup("#instructionBox");
		idField = (TextField) fieldsBox.lookup("#idField");
		firstNameField = (TextField) fieldsBox.lookup("#firstNameField");
		emailField = (TextField) fieldsBox.lookup("#emailField");
		lastNameField = (TextField) fieldsBox.lookup("#lastNameField");
		passwd1Field = (PasswordField) fieldsBox.lookup("#passwd1Field");
		passwd2Field = (PasswordField) fieldsBox.lookup("#passwd2Field");

		HBox signUpControlBox = (HBox) fieldsBox.lookup("#signUpControlBox");
		signUpBtn = (Button) signUpControlBox.lookup("#signUpBtn");
		cancelBtn = (Button) signUpControlBox.lookup("#cancelBtn");
		signUpControlBox.prefWidthProperty().bind(passwd2Field.widthProperty().multiply(1));
		fieldsBox.prefWidthProperty().bind(infoEntryBox.widthProperty().multiply(0.5));
		instructionBox.prefWidthProperty().bind(infoEntryBox.widthProperty().multiply(0.5));

		// Sign Up Button Listener
		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				UserVO user = new UserVO();
				user.setFirstName(firstNameField.getText().toString());
				user.setLastName(lastNameField.getText().toString());
				user.setNinerId(idField.getText().toString());
				user.setEmail(emailField.getText().toString());
				user.setPassword(passwd1Field.getText().toString());
				
				UserRegisterationClient signUpClient = new UserRegisterationClient();
				int status = signUpClient.validate(user, passwd2Field.getText().trim());
				if(status==1){
					System.out.println("Validations Successful");
					System.out.println("Sign Up Pressed");
					System.out.println(user.getNinerId() + " " + user.getFirstName() + " " + user.getLastName() + " "
							+ user.getEmail() + " " + user.getPassword());
					presenter.sendSignUpMessage(user);
				}
				else{
					Alerts.validations(status);
					System.out.println("Validations Error");
				}
				
			
			}
		});

		// Cancel Button Listener
		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				presenter.loadHomepage();
			}
		});
		
		
		Scene scene = new Scene(signUpBox, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class
				.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);
		cancelBtn.requestFocus();

	}

}
