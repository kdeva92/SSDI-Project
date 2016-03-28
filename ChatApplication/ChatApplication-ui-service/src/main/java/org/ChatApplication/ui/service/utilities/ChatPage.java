package org.ChatApplication.ui.service.utilities;

import java.io.IOException;

import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.Message;
import org.ChatApplication.ui.service.models.User;
import org.ChatApplication.ui.service.observer.MessageListener;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ChatPage {
	TextArea messageBox;
	TextField searchUserField;
	String user_name,id;
	public TableView<Message> chatString;
	public TableView<Contact> savedContacts;
	public ObservableList<Contact> conT;
	public ObservableList<Message> dataT;
	Thread sendThread;
	Thread recieveThread;
	Button sendButton;
	String srchUser;
	

	void loadChatPage(String chatUser,String ninerID) throws IOException {
		user_name = chatUser;
		id = ninerID;
		
		/*
		 * UI Elements discovery
		 */
		
		HBox ChatPane = (HBox) FXMLLoader.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/ChatWindow.fxml"));
		VBox contactsBox = (VBox) ChatPane.lookup("#contactsBox");
		VBox chatBox = (VBox) ChatPane.lookup("#chatBox");
		HBox msgSendBox = (HBox)chatBox.lookup("#msgSendBox");
		VBox msgControlBox = (VBox) msgSendBox.lookup("#msgControlBox");
		VBox chatTableBox = (VBox) ChatPane.lookup("#ChatTableBox");
		sendButton = (Button) msgControlBox.lookup("#sendBtn");
		sendButton.setStyle("-fx-background-color: #d8bfd8");
		Button attachBtn = (Button) msgControlBox.lookup("#attachBtn");
		attachBtn.setStyle("-fx-background-color: #d8bfd8");
		messageBox = (TextArea)  msgSendBox.lookup("#messageBox");
		VBox ContactsListBox = (VBox) contactsBox.lookup("#ContactsListBox");
		
		
		//msgSendBox.prefWidthProperty().bind(chatBox.widthProperty().multiply(0.1));
		//chatTableBox.prefWidthProperty().bind(chatBox.widthProperty().multiply(0.9));
		
	
		/*
		 * Message Send triggers
		 */
		
	sendButton.setOnAction(new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			SenderController sender = new SenderController();
			sender.sendChatMessage(id, "Anonymous", messageBox.getText().trim(), ReceiverTypeEnum.INDIVIDUAL_MSG);
			//dataT.add(new Message(user_name, null, messageBox.getText().trim()));
			
			//chatString.setItems(dataT);
			
			//MessageListener listener = MessageListener.getInstance();
			//listener.updateUI(id, messageBox.getText().trim());
			messageBox.setText("");
			messageBox.positionCaret(0);

		}
	});
		
		
	messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

		public void handle(KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getCode() == KeyCode.ENTER) {
				SenderController sender = new SenderController();
				sender.sendChatMessage(id, "Anonymous", messageBox.getText().trim(), ReceiverTypeEnum.INDIVIDUAL_MSG);
				//dataT.add(new Message(user_name, "Anonymous", messageBox.getText().trim()));
			//	MessageListener listener = MessageListener.getInstance();
				//listener.updateUI(id, messageBox.getText().trim());
				messageBox.setText("");
				messageBox.positionCaret(0);
			}
		}

	});


		/*
		 * Chat Area
		 */

		chatString = new TableView<Message>();
		

		TableColumn messageCol = new TableColumn("Message");
		TableColumn userCol = new TableColumn("User");
		userCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.15));
		messageCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.85));
		dataT = FXCollections.observableArrayList();
		chatString.setItems(dataT);
		userCol.setCellValueFactory(new PropertyValueFactory("sender"));
		messageCol.setCellValueFactory(new PropertyValueFactory("messageBody"));

		chatString.getColumns().addAll(userCol, messageCol);
		

		chatTableBox.getChildren().add(chatString);
		chatTableBox.setVgrow(chatString, Priority.ALWAYS);
		ChatPane.setHgrow(chatBox, Priority.ALWAYS);
		
	
		/*
		 * Contact Area
		 */
		
		savedContacts = new TableView<Contact>();
		
		TableColumn contactCol = new TableColumn("My Contacts");
		contactCol.prefWidthProperty().bind(savedContacts.widthProperty().multiply(1));
		conT = FXCollections.observableArrayList();
		savedContacts.setItems(conT);
		contactCol.setCellValueFactory(new PropertyValueFactory("name"));
		savedContacts.getColumns().add(contactCol);
		ContactsListBox.getChildren().add(savedContacts);
		ContactsListBox.setVgrow(savedContacts, Priority.ALWAYS);
		
		

		Scene scene = new Scene(ChatPane, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);

	}

}
