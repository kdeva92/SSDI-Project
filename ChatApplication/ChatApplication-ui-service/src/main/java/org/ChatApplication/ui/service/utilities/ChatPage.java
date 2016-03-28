package org.ChatApplication.ui.service.utilities;

import java.io.IOException;

import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.models.Message;

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
	String user_name;
	public TableView<Message> chatString;
	public ObservableList<Message> dataT;
	Thread sendThread;
	Thread recieveThread;
	Button sendButton;
	String srchUser;

	void loadChatPage(String username) throws IOException {
		user_name = username;
		
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
		sendButton.setStyle("-fx-background-color: #000000");
		messageBox = (TextArea)  msgSendBox.lookup("#messageBox");
	
		/*
		 * Message Send triggers
		 */
		
	sendButton.setOnAction(new EventHandler<ActionEvent>() {

		public void handle(ActionEvent event) {
			// TODO Auto-generated method stub
			ChatClient cc = new ChatClient();
			dataT.add(new Message(user_name, null, messageBox.getText().toString()));
			
			chatString.setItems(dataT);
			cc.sendMessage(user_name, messageBox.getText());
			messageBox.clear();

		}
	});
		
		
	messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

		public void handle(KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getCode() == KeyCode.ENTER) {
				ChatClient cc = new ChatClient();
				dataT.add(new Message(user_name, "Anonymous", messageBox.getText().trim()));
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
		final ObservableList<Message> data = FXCollections.observableArrayList(new Message("A", "B", "C"));

		chatTableBox.getChildren().add(chatString);
		chatTableBox.setVgrow(chatString, Priority.ALWAYS);
		ChatPane.setHgrow(chatBox, Priority.ALWAYS);

	
		/*
		 * Contact Area
		 */
		
		

		

		Scene scene = new Scene(ChatPane, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);

	}

}
