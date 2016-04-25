package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.User;

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
	ObservableList<Contact> contactList;
	Button addMemberButton;
	Button removeMemberButton;
	Button cancelBtn;
	Button createBtn;
	public Presenter presenter;
	Set<String> listOfMembers;

	public void loadCreateGroupPage(Presenter present) throws IOException {
		listOfMembers = new HashSet<String>();
		this.presenter = present;
		groupContainer = (VBox) FXMLLoader
				.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/CreateGroupWindow.fxml"));

		HBox controlBox = (HBox) groupContainer.lookup("#controlBox");

		VBox selectBox = (VBox) controlBox.lookup("#selectBox");
		VBox selectResultBox = (VBox) controlBox.lookup("#selectResultBox");

		groupNameText = (TextField) selectBox.lookup("#groupNameField");
		memberPicker = (ComboBox) selectBox.lookup("#memberPicker");
		addMemberButton = (Button) selectBox.lookup("#addMemberButton");
		removeMemberButton = (Button) selectBox.lookup("#removeMemberButton");

		HBox buttonBox = (HBox) groupContainer.lookup("#buttonBox");

		createBtn = (Button) buttonBox.lookup("#createBtn");
		cancelBtn = (Button) buttonBox.lookup("#cancelBtn");
		memberPicker.getItems().clear();
		UserVO user = new UserVO();
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		Connection conn = dbConnector.getConn();
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * from Contacts_"+this.presenter.getUser().getNinerId());
			while (rs.next()) {
				memberPicker.getItems().add(new Contact(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Table Creation for Contacts
		 */

		selectedContacts = new TableView<UserVO>();

		TableColumn lastnameCol = new TableColumn("Last Name");
		TableColumn firstnameCol = new TableColumn("First Name");
		
		lastnameCol.prefWidthProperty().bind(selectedContacts.widthProperty().multiply(0.70));
		firstnameCol.prefWidthProperty().bind(selectedContacts.widthProperty().multiply(0.30));

		contactList = FXCollections.observableArrayList();
		selectedContacts.setItems(contactList);

		lastnameCol.setCellValueFactory(new PropertyValueFactory("lastName"));
		firstnameCol.setCellValueFactory(new PropertyValueFactory("firstName"));

		selectedContacts.getColumns().addAll(firstnameCol,lastnameCol);
		selectResultBox.getChildren().add(selectedContacts);

		/*
		 * Add member button listener
		 */
		addMemberButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				Contact selectedUser = (Contact) memberPicker.getValue();
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
				Contact selectedUser = (Contact) selectedContacts.getSelectionModel().getSelectedItem();
				memberPicker.getItems().add(selectedUser);
				contactList.remove(selectedUser);

			}
		});

		createBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				listOfMembers.clear();
				listOfMembers.add(presenter.getUser().getNinerId());
				String groupName = groupNameText.getText();
				for (Contact con : contactList) {
					listOfMembers.add(con.getNinerID());
				}
				ArrayList<String> membersList = new ArrayList<String>(listOfMembers);
				presenter.sendCreateGroupMessage(new GroupVO(groupNameText.getText().trim(), membersList));
				
			}
		});

		cancelBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				presenter.loadChatPage();

			}
		});

		Scene scene = new Scene(groupContainer, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class
				.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);
	}

}
