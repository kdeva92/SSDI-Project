package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.communication.RecieveFromServer;
import org.ChatApplication.ui.service.communication.SendToServer;
import org.ChatApplication.ui.service.models.Message;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatPage {
	TextArea messageBox;
	TextField searchUserField;
	String user_name;
	public TableView<Message> chatString;
	public ObservableList dataT;
	Thread sendThread;
	Thread recieveThread;
	Button sendButton;
	String srchUser;

	void loadChatPage(String username) {
		// /*
		// * Connectivity Code
		// */
		// Socket clientSocket;
		// try {
		// clientSocket = new Socket("XYZ",8080);
		// SendToServer sender = new SendToServer(clientSocket);
		// RecieveFromServer reciever = new RecieveFromServer(clientSocket);
		// recieveThread = new Thread(reciever);
		// recieveThread.start();
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		user_name = username;

		BorderPane ChatPane = new BorderPane();

		// Pane Design
		VBox left = new VBox();
		left.setAlignment(Pos.TOP_CENTER);

		VBox middle = new VBox();
		middle.setAlignment(Pos.CENTER);
		middle.setPadding(new Insets(50, 50, 50, 50));

		HBox bottom = new HBox();

		bottom.setAlignment(Pos.CENTER);
		bottom.setPadding(new Insets(10, 100, 50, 100));
		bottom.setSpacing(20);

		HBox top = new HBox();
		top.setAlignment(Pos.CENTER);

		ChatPane.setLeft(left);

		ChatPane.setCenter(middle);
		ChatPane.setTop(top);
		ChatPane.setBottom(bottom);

		/*
		 * Bottom Area
		 */

		sendButton = new Button("Send Message");
		sendButton.setPrefSize(200, 30);
		sendButton.setStyle("-fx-font: 18 arial; -fx-base:  #b0e0e6");
		messageBox = new TextArea();
		messageBox.setWrapText(true);
		messageBox.setPrefSize(400, 100);
		bottom.getChildren().addAll(messageBox, sendButton);
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

		sendButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				ChatClient cc = new ChatClient();
				dataT.add(new Message(user_name, null, messageBox.getText().toString()));
				messageBox.clear();
				// chatString.setItems(dataT);
				// cc.sendMessage(user_name, messageBox.getText());

			}
		});

		/*
		 * Middle Area
		 */

		chatString = new TableView<Message>();
		chatString.setEditable(true);
		chatString.setVisible(true);

		TableColumn userCol = new TableColumn("User");
		TableColumn messageCol = new TableColumn("Message");
		userCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.15));
		messageCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.85));
		dataT = FXCollections.observableArrayList();
		chatString.setItems(dataT);
		userCol.setCellValueFactory(new PropertyValueFactory("sender"));
		messageCol.setCellValueFactory(new PropertyValueFactory("messageBody"));

		chatString.getColumns().addAll(userCol, messageCol);
		final ObservableList<Message> data = FXCollections.observableArrayList(new Message("A", "B", "C"));

		middle.getChildren().add(chatString);

		/*
		 * Top Area
		 */

		top.getChildren().add(new Label(username + "'s ChatPage"));

		/*
		 * Left Area
		 */

		left.setSpacing(20);

		Label srchH = new Label("User Search");
		searchUserField = new TextField();
		searchUserField.setPromptText("Enter Niner ID or Email ID");
		messageBox.requestFocus();
		Button searchBtn = new Button("Search User");
		Button add2contacts = new Button("Add to Contacts");

		HBox srchOperator = new HBox();
		srchOperator.setSpacing(10);
		srchOperator.getChildren().addAll(searchBtn, add2contacts);

		VBox searchBox = new VBox();
		searchBox.setSpacing(10);
		searchBox.getChildren().addAll(srchH, searchUserField, srchOperator);
		VBox contacts = new VBox();

		left.getChildren().addAll(searchBox, contacts);

		srchUser = "Sample";

		searchBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				ContactsHandler ch = new ContactsHandler();
				ch.searchUser(srchUser);
			}
		});

		Scene scene = new Scene(ChatPane, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class
				.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);

	}

}
