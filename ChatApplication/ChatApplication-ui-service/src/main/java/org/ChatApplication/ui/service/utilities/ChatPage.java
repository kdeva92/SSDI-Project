package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.MessageVO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
	@SuppressWarnings("restriction")
	public Presenter presenter;
	TextArea messageBox;
	public User user;
	String user_name, id;
	public TableView<MessageVO> chatString;
	public TableView<Contact> savedContacts;
	public ObservableList<Contact> conT;
	public ObservableList<MessageVO> dataT;
	Button logoutBtn;
	Button sendButton;
	Button crtGrpBtn;
	Button srchBtn;
	String srchUser;
	private ServerController serverController;
	private SenderController senderController;
	TextField searchUserT;
	HashMap<String, ArrayList<MessageVO>> chatsMap = new HashMap<String, ArrayList<MessageVO>>();

	@SuppressWarnings("restriction")
	public void loadChatPage(Presenter present, User user1) throws IOException {
		this.presenter = present;
		this.user = user1;

		/*
		 * UI Elements discovery
		 */

		HBox ChatPane = (HBox) FXMLLoader
				.load(Login.class.getResource("/org/ChatApplication/ui/service/stylesheets/ChatWindow.fxml"));
		VBox contactsBox = (VBox) ChatPane.lookup("#contactsBox");
		VBox chatBox = (VBox) ChatPane.lookup("#chatBox");
		HBox msgSendBox = (HBox) chatBox.lookup("#msgSendBox");
		VBox msgControlBox = (VBox) msgSendBox.lookup("#msgControlBox");
		VBox chatTableBox = (VBox) ChatPane.lookup("#ChatTableBox");
		sendButton = (Button) msgControlBox.lookup("#sendBtn");
		sendButton.setStyle("-fx-background-color: #d8bfd8");
		Button attachBtn = (Button) msgControlBox.lookup("#attachBtn");
		attachBtn.setStyle("-fx-background-color: #d8bfd8");
		messageBox = (TextArea) msgSendBox.lookup("#messageBox");
		VBox ContactsListBox = (VBox) contactsBox.lookup("#ContactsListBox");

		VBox searchBox = (VBox) contactsBox.lookup("#searchBox");
		HBox searchControlBox = (HBox) searchBox.lookup("#searchControlBox");

		searchUserT = (TextField) searchBox.lookup("#searchUserT");
		srchBtn = (Button) searchControlBox.lookup("#srchBtn");
		crtGrpBtn = (Button) searchControlBox.lookup("#crtGrpBtn");

		HBox userHeading = (HBox) chatBox.lookup("#userHeading");
		HBox userLabelBox = (HBox) userHeading.lookup("#userLabelBox");
		HBox basicButtonBox = (HBox) userHeading.lookup("#basicButtonBox");

		userLabelBox.prefWidthProperty().bind(userHeading.widthProperty().multiply(0.8));
		basicButtonBox.prefWidthProperty().bind(userHeading.widthProperty().multiply(0.2));

		Label usernameLabel = (Label) userLabelBox.lookup("#usernameLabel");
		usernameLabel.setText(
				"Logged in as " + this.user.getFirstName().trim() + "( " + this.user.getNinerId().trim() + " )");
		logoutBtn = (Button) basicButtonBox.lookup("#logoutBtn");

		// msgSendBox.prefWidthProperty().bind(chatBox.widthProperty().multiply(0.1));
		// chatTableBox.prefWidthProperty().bind(chatBox.widthProperty().multiply(0.9));

		/*
		 * Message Send triggers
		 */

		sendButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// String niner = user.getNinerId();
				// MessageVO mess = new MessageVO(niner, "000000001",
				// messageBox.getText().trim());
				// dataT.add(mess);
				// presenter.sendChatMessage(niner, "000000001",
				// messageBox.getText().trim(),
				// ReceiverTypeEnum.INDIVIDUAL_MSG);
				// // dataT.add(new Message(user_name, null,
				// // messageBox.getText().trim()));
				//
				// // chatString.setItems(dataT);
				//
				// // MessageListener listener = MessageListener.getInstance();
				// // listener.updateUI(id, messageBox.getText().trim());
				// messageBox.setText("");
				// messageBox.positionCaret(0);

				sendingModule();

			}
		});

		messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				if (event.getCode() == KeyCode.ENTER) {
					// String niner = user.getNinerId();
					// MessageVO mess = new MessageVO(niner, "000000001",
					// messageBox.getText().trim());
					// dataT.add(mess);
					// presenter.sendChatMessage(niner, "000000001",
					// messageBox.getText().trim(),
					// ReceiverTypeEnum.INDIVIDUAL_MSG);
					// // dataT.add(new Message(user_name, "Anonymous",
					// // messageBox.getText().trim()));
					// // MessageListener listener =
					// MessageListener.getInstance();
					// // listener.updateUI(id, messageBox.getText().trim());
					// messageBox.setText("");
					// messageBox.positionCaret(0);
					sendingModule();
				}
			}

		});

		/*
		 * Create Group Triggers
		 */

		crtGrpBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				// presenter.createGrp();
			}
		});

		/*
		 * Search Contact Trigger
		 */

		srchBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (searchUserT.getText() != null && !searchUserT.getText().isEmpty()) {
					presenter.searchContact(searchUserT.getText().trim());
				}
			}
		});

		/*
		 * Chat Area
		 */

		chatString = new TableView<MessageVO>();

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

		loadContacts();

		savedContacts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				Contact contact = (Contact) newValue;
				// System.out.println(contact.getNinerID()+contact.getName()+contact.getEmail());

			}
		});

		Scene scene = new Scene(ChatPane, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class
				.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);

	}

	private void sendingModule() {
		Contact contact = (Contact) savedContacts.getSelectionModel().selectedItemProperty().get();
		System.out.println("Sending to :" + contact.getNinerID() + "\t" + contact.getName());
		String niner = user.getNinerId();
		MessageVO mess = new MessageVO(niner, contact.getNinerID(), messageBox.getText().trim());
		dataT.add(mess);
		presenter.sendChatMessage(niner, contact.getNinerID(), messageBox.getText().trim(),
				ReceiverTypeEnum.INDIVIDUAL_MSG);
		// dataT.add(new Message(user_name, "Anonymous",
		// messageBox.getText().trim()));
		// MessageListener listener = MessageListener.getInstance();
		// listener.updateUI(id, messageBox.getText().trim());
		messageBox.setText("");
		messageBox.positionCaret(0);
	}

	private void loadContacts() {

		DatabaseConnecter d = new DatabaseConnecter();
		Connection conn = d.getConn();

		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * from User");

			while (rs.next()) {
				// System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3));
				// cont.setEmail(rs.getString(3));
				// cont.setName(rs.getString(2));
				// cont.setNinerID(rs.getString(1));

				conT.add(new Contact(rs.getString(1), rs.getString(2), rs.getString(3)));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void renderSearchAlert(List<User> users) {
		if (users != null && users.isEmpty()) {
			Alerts.createInformationAlert("No Contact found", null, null);
		} else {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("User Found");
			alert.setHeaderText("Do you want to add the User as your contact?");
			ButtonType buttonYes = new ButtonType("Yes");
			ButtonType buttonNo = new ButtonType("No");
			alert.getButtonTypes().setAll(buttonYes, buttonNo);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonYes) {
				presenter.addToContact(users.get(0));
			} else {
				alert.close();
			}

		}
	}

}
