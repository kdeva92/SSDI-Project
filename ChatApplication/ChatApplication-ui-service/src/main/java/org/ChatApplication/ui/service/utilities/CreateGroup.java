package org.ChatApplication.ui.service.utilities;

import java.io.IOException;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.models.Contact;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CreateGroup {
VBox groupContainer;
TextField groupNameText;
ComboBox memberPicker;
TableView selectedContacts;
ObservableList<User> contactList;
Button addMemberButton;
Button removeMemberButton;

public void loadCreateGroupPage() throws IOException
{
groupContainer = (VBox) FXMLLoader.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/CreateGroupWindow.fxml"));	

HBox controlBox = (HBox) groupContainer.lookup("#controlBox");

VBox selectBox = (VBox) controlBox.lookup("#selectBox");
VBox selectResultBox = (VBox) controlBox.lookup("#selectResultBox");

groupNameText = (TextField) selectBox.lookup("#groupNameText");
memberPicker = (ComboBox) selectBox.lookup("#memberPicker");
addMemberButton = (Button) selectBox.lookup("#addMemberButton");
removeMemberButton = (Button) selectBox.lookup("#removeMemberButton");



/*
 * Table Creation for Contacts
 */

selectedContacts = new TableView<User>();

TableColumn ninerIDCol = new TableColumn("Niner ID");
TableColumn nameCol = new TableColumn("User");
TableColumn emailCol = new TableColumn("Email ID");

ninerIDCol.prefWidthProperty().bind(selectedContacts.widthProperty().multiply(0.20));
emailCol.prefWidthProperty().bind(selectedContacts.widthProperty().multiply(0.40));
nameCol.prefWidthProperty().bind(selectedContacts.widthProperty().multiply(0.40));

contactList = FXCollections.observableArrayList();
selectedContacts.setItems(contactList);

ninerIDCol.setCellValueFactory(new PropertyValueFactory("ninerId"));
emailCol.setCellValueFactory(new PropertyValueFactory("email"));
nameCol.setCellValueFactory(new PropertyValueFactory("firstName"));

selectedContacts.getColumns().addAll(ninerIDCol,emailCol,nameCol);



/*
 * Add member button listener
 */
addMemberButton.setOnAction(new EventHandler<ActionEvent>() {
	
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		User selectedUser = (User)memberPicker.getValue();
		contactList.add(selectedUser);
		memberPicker.getItems().remove(selectedUser);
	}
});

/*
 * remove member button listener
 */
removeMemberButton.setOnAction(new EventHandler<ActionEvent>() {
	
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		User selectedUser = (User)selectedContacts.getSelectionModel().getSelectedItem();
		memberPicker.getItems().add(selectedUser);
		contactList.remove(selectedUser);
		
	}
});

Scene scene = new Scene(groupContainer, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

ChatApp.stage.setScene(scene);
}

	
	
}
