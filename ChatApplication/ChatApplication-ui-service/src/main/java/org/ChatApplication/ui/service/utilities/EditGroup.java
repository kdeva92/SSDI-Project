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
import org.ChatApplication.ui.service.models.GroupTableObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EditGroup {
	GroupVO groupVO;
	VBox groupContainer;
	Presenter presenter;
	TextField groupNameText;
	ComboBox memberPicker;
	TableView selectedContacts;
	ObservableList<Contact> contactList;
	Button addMemberButton;
	Button removeMemberButton;
	Button cancelBtn;
	Button createBtn;
	ObservableList<Contact> contacts;
	Set<String> listOfMembers;
	GroupTableObject groupObject;
	
	public void loadEditGroup(GroupTableObject group,Presenter present,ObservableList<Contact> conT) throws IOException{
		this.presenter = present;
		this.listOfMembers = new HashSet<String>();
		this.groupObject = group;
		contacts = conT;
		groupContainer = (VBox) FXMLLoader.load(EditGroup.class.getResource("/org/ChatApplication/ui/service/stylesheets/CreateGroupWindow.fxml"));
		
		HBox headingBox = (HBox)groupContainer.lookup("#headingBox");
		Label headingLbl = (Label) headingBox.lookup("#headingLbl");
		headingLbl.setText("Edit Group");
		
		
		HBox controlBox = (HBox) groupContainer.lookup("#controlBox");

		VBox selectBox = (VBox) controlBox.lookup("#selectBox");
		VBox selectResultBox = (VBox) controlBox.lookup("#selectResultBox");

		groupNameText = (TextField) selectBox.lookup("#groupNameField");
		memberPicker = (ComboBox) selectBox.lookup("#memberPicker");
		addMemberButton = (Button) selectBox.lookup("#addMemberButton");
		removeMemberButton = (Button) selectBox.lookup("#removeMemberButton");

		HBox buttonBox = (HBox) groupContainer.lookup("#buttonBox");

		createBtn = (Button) buttonBox.lookup("#createBtn");
		createBtn.setText("Confirm Edit");
		
		cancelBtn = (Button) buttonBox.lookup("#cancelBtn");
		memberPicker.getItems().clear();
		
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
		
		populateGroupDetails(group);
		
		
		
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
				if(!groupNameText.getText().isEmpty()){
				listOfMembers.clear();
				listOfMembers.add(presenter.getUser().getNinerId());
				String groupName = groupNameText.getText();
				for (Contact con : contactList) {
					listOfMembers.add(con.getNinerID());
				}
				ArrayList<String> membersList = new ArrayList<String>(listOfMembers);
				GroupVO grp = new GroupVO(groupNameText.getText().trim(), membersList);
				grp.setGroupId(groupObject.getGroupID());
				System.out.println("Group being sent: "+grp.getGroupId()+" "+grp.getGroupName());
				//presenter.sendCreateGroupMessage());
				presenter.sendEditGroupMessage(grp);
				}
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
	
	
	
	private void populateGroupDetails(GroupTableObject group) {
		ArrayList<String> members = group.getMembers();
		this.groupNameText.setText(group.getGroupName());
		for(Contact con : this.contacts){
			if(members.contains(con.getNinerID().trim())){
				contactList.add(con);
			}
			else{
				memberPicker.getItems().add(con);
			}
		}
		
	}
}
