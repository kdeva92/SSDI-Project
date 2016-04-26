package org.ChatApplication.ui.service.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.GroupTableObject;
import org.ChatApplication.ui.service.models.MessageVO;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class ChatPage {
	@SuppressWarnings("restriction")
	public Presenter presenter;
	TextArea messageBox;
	public UserVO user;
	String user_name, id;
	public TableView<MessageVO> chatString;
	public TableView<Contact> savedContacts;
	public TableView<GroupTableObject> savedGroups;

	public ObservableList<Contact> conT;
	public ObservableList<MessageVO> dataT;
	public ObservableList<GroupTableObject> groupT;
	private Desktop desktop = Desktop.getDesktop();
	HashMap<String, ObservableList<MessageVO>> userChats = new HashMap<String, ObservableList<MessageVO>>();
	Button logoutBtn;
	Button sendButton;
	Button crtGrpBtn;
	Button srchBtn;
	Button attachBtn;
	String srchUser;
	private ServerController serverController;
	private SenderController senderController;
	TextField searchUserT;
	// HashMap<String, ArrayList<MessageVO>> chatsMap = new HashMap<String,
	// ArrayList<MessageVO>>();

	@SuppressWarnings("restriction")
	public void loadChatPage(Presenter present, UserVO user1) throws IOException {
		this.presenter = present;
		this.user = user1;
		createUserContactList();
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
		attachBtn = (Button) msgControlBox.lookup("#attachBtn");
		attachBtn.setStyle("-fx-background-color: #d8bfd8");
		messageBox = (TextArea) msgSendBox.lookup("#messageBox");
		VBox ContactsListBox = (VBox) contactsBox.lookup("#ContactsListBox");
		VBox GroupsListBox = (VBox) contactsBox.lookup("#GroupsListBox");

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
				// Message1 mess = new Message1(niner, "000000001",
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
					// Message1 mess = new Message1(niner, "000000001",
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
		 * Attach Button
		 */
		final FileChooser fileChooser = new FileChooser();
		attachBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

				configureFileChooser(fileChooser);
				File file = fileChooser.showOpenDialog(ChatApp.stage);
				if (file != null) {
					openFile(file);
				}
			}
		});

		/*
		 * Create Group Triggers
		 */

		crtGrpBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub

				presenter.loadCreateGroup();
			}
		});

		/*
		 * Search Contact Trigger
		 */

		srchBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				presenter.searchContact(searchUserT.getText().trim());
			}
		});

		logoutBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				presenter.handleTermination();

			}
		});

		/*
		 * Chat Area
		 */

		// class XCell extends TableCell<String, String> {
		// @Override
		// protected void updateItem(String item, boolean empty) {
		// super.updateItem(item, empty);
		// this.setText(item);
		// this.setTooltip((empty || item == null) ? null : new Tooltip(item));
		// }
		// }

		chatString = new TableView<MessageVO>();

		TableColumn messageCol = new TableColumn("Message");
		TableColumn userCol = new TableColumn("User");
		userCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.15));
		messageCol.prefWidthProperty().bind(chatString.widthProperty().multiply(0.85));
		dataT = FXCollections.observableArrayList();
		chatString.setItems(dataT);
		userCol.setCellValueFactory(new PropertyValueFactory("senderName"));
		messageCol.setCellValueFactory(new PropertyValueFactory("messageBody"));

		chatString.getColumns().addAll(userCol, messageCol);

		// messageCol.setCellValueFactory(
		// new Callback<TableColumn.CellDataFeatures<String, String>,
		// ObservableValue<String>>() {
		//
		// public ObservableValue<String> call(CellDataFeatures<String, String>
		// arg0) {
		//
		// return new ReadOnlyStringWrapper(arg0.getValue());
		// }
		// });
		//
		// messageCol.setCellFactory(new Callback<TableColumn<String, String>,
		// TableCell<String, String>>() {
		// public TableCell<String, String> call(TableColumn<String, String>
		// param) {
		// return new XCell();
		// }
		// });

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
		contactCol.setCellValueFactory(new PropertyValueFactory("firstName"));
		savedContacts.getColumns().add(contactCol);
		ContactsListBox.getChildren().add(savedContacts);
		ContactsListBox.setVgrow(savedContacts, Priority.ALWAYS);

		/*
		 * Group Area
		 */

		savedGroups = new TableView<GroupTableObject>();

		TableColumn nameCol = new TableColumn("My Groups");
		nameCol.prefWidthProperty().bind(savedGroups.widthProperty().multiply(1));
		groupT = FXCollections.observableArrayList();
		savedGroups.setItems(groupT);
		nameCol.setCellValueFactory(new PropertyValueFactory("groupName"));
		savedGroups.getColumns().add(nameCol);
		GroupsListBox.getChildren().add(savedGroups);
		GroupsListBox.setVgrow(savedGroups, Priority.ALWAYS);

		loadContacts();

		// Handling Auto Scroll
		chatString.getItems().addListener(new ListChangeListener<MessageVO>() {

			public void onChanged(javafx.collections.ListChangeListener.Change<? extends MessageVO> c) {
				System.out.println("Scrolling");
				chatString.scrollTo(c.getList().size() - 1);

			}

		});

		// Handling user to Chat binding

		// Handling user to Chat binding

		savedContacts.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Contact>() {

			public void changed(ObservableValue<? extends Contact> observable, Contact oldValue, Contact newValue) {
				// dataT.clear();
				Contact contact = (Contact) newValue;
				String niner = contact.getNinerID();
				ObservableList<MessageVO> messageList = userChats.get(niner);
				chatString.setItems(messageList);

			}
		});

		// Handling group to chat binding

		savedGroups.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<GroupTableObject>() {
			public void changed(ObservableValue<? extends GroupTableObject> observable, GroupTableObject oldValue,
					GroupTableObject newValue) {
				// dataT.clear();
				GroupTableObject group = (GroupTableObject) newValue;
				String groupId = group.getGroupID() + "";
				ObservableList<MessageVO> messageList = userChats.get(groupId);
				chatString.setItems(messageList);

			}
		});

		Scene scene = new Scene(ChatPane, ChatApp.stage.getWidth(), ChatApp.stage.getHeight());
		scene.getStylesheets().add(ChatApp.class
				.getResource("/org/ChatApplication/ui/service/stylesheets/Basic_Style.css").toExternalForm());

		ChatApp.stage.setScene(scene);

	}

	private void createUserContactList() {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		Connection conn = dbConnector.getConn();
		try {
			Statement stat = conn.createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS Contacts_" + user.getNinerId()
					+ "(niner_id varchar(10),first_name varchar(60),last_name varchar(60),email varchar(60))");
			System.out.println("Table Created as:\n" + "CREATE TABLE IF NOT EXISTS Contacts_" + user.getNinerId()
					+ "(niner_id varchar(10),first_name varchar(60),last_name varchar(60),email varchar(60))");

			stat.execute("CREATE TABLE IF NOT EXISTS Groups_" + user.getNinerId()
					+ "(group_id varchar(10),group_name varchar(60),members varchar(200))");
			System.out.println("Table Created as:\n" + "CREATE TABLE IF NOT EXISTS Groups_" + user.getNinerId()
					+ "(group_id int,group_name varchar(60),members varchar(200))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendingModule() {
		Contact contact = (Contact) savedContacts.getSelectionModel().selectedItemProperty().get();
		if (contact == null) {
			GroupTableObject group = savedGroups.getSelectionModel().selectedItemProperty().get();

			System.out.println("Sending to :" + group.getGroupID() + "\t" + group.getGroupName());
			String niner = user.getNinerId();
			MessageVO mess = new MessageVO(niner, user.getFirstName(), messageBox.getText().trim());
			ObservableList<MessageVO> chatList = userChats.get(group.getGroupID() + "");
			chatList.add(mess);
			chatString.scrollTo(chatList.size() - 1);
			presenter.sendChatMessage(niner, group.getGroupID() + "", messageBox.getText().trim(),
					ReceiverTypeEnum.GROUP_MSG);
			DatabaseConnecter dbConnector = new DatabaseConnecter();
			Connection conn = dbConnector.getConn();
			try {
				Statement stat = conn.createStatement();
				stat.execute("INSERT INTO Grp_" + user.getNinerId() + "_" + group.getGroupID() + " VALUES('"
						+ user.getNinerId() + "','" + user.getFirstName() + "','" + messageBox.getText().trim() + "')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			System.out.println("Sending to :" + contact.getNinerID() + "\t" + contact.getFirstName());
			String niner = user.getNinerId();
			MessageVO mess = new MessageVO(niner, user.getFirstName(), messageBox.getText().trim());
			ObservableList<MessageVO> chatList = userChats.get(contact.getNinerID().trim());
			chatList.add(mess);
			chatString.scrollTo(chatList.size() - 1);
			presenter.sendChatMessage(niner, contact.getNinerID(), messageBox.getText().trim(),
					ReceiverTypeEnum.INDIVIDUAL_MSG);
			DatabaseConnecter dbConnector = new DatabaseConnecter();
			Connection conn = dbConnector.getConn();
			try {
				Statement stat = conn.createStatement();
				stat.execute("CREATE TABLE IF NOT EXISTS Chat_" + user.getNinerId() + "_" + contact.getNinerID()
						+ "(sender varchar(10),messageBody varchar(500))");
				stat.execute("INSERT INTO Chat_" + user.getNinerId() + "_" + contact.getNinerID() + " VALUES('"
						+ user.getNinerId() + "','" + user.getFirstName() + "','" + messageBox.getText().trim() + "')");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

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
			ResultSet rs = st.executeQuery("SELECT * from Contacts_" + user.getNinerId());

			while (rs.next()) {
				// System.out.println(rs.getString(1)+rs.getString(2)+rs.getString(3));
				// cont.setEmail(rs.getString(3));
				// cont.setName(rs.getString(2));
				// cont.setNinerID(rs.getString(1));
				ObservableList<MessageVO> messages = FXCollections.observableArrayList();
				Statement st2 = conn.createStatement();
				ResultSet rs2 = st2.executeQuery("SELECT * from Chat_" + user.getNinerId() + "_" + rs.getString(1));
				while (rs2.next()) {
					messages.add(new MessageVO(rs2.getString(1), rs2.getString(2), rs2.getString(3)));
				}
				for (MessageVO me : messages) {
					System.out.println(me.getSender() + " " + me.getSenderName() + " " + me.getMessageBody());
				}
				userChats.put(rs.getString(1), messages);
				conT.add(new Contact(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));

			}

			ResultSet rs1 = st.executeQuery("SELECT * from Groups_" + user.getNinerId());

			while (rs1.next()) {
				ObservableList<MessageVO> messages = FXCollections.observableArrayList();
				Statement st3 = conn.createStatement();
				ResultSet rs3 = st3.executeQuery("SELECT * from Grp_" + user.getNinerId() + "_" + rs1.getInt(1));
				while (rs3.next()) {
					messages.add(new MessageVO(rs3.getString(1), rs3.getString(2), rs3.getString(3)));
				}
				for (MessageVO me : messages) {
					System.out.println(me.getSender() + " " + me.getSenderName() + " " + me.getMessageBody());
				}
				userChats.put(rs1.getInt(1) + "", messages);
				groupT.add(new GroupTableObject(rs1.getInt(1), rs1.getString(2), rs1.getString(3)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void renderSearchAlert(List<UserVO> users) {
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

		srchBtn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				if (searchUserT.getText() != null && !searchUserT.getText().isEmpty()) {
					presenter.searchContact(searchUserT.getText().trim());
				}
				searchUserT.clear();
			}
		});

	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

	}

	private void openFile(File file) {
		Contact contact = (Contact) savedContacts.getSelectionModel().selectedItemProperty().get();
		MessageVO mess = new MessageVO(user.getNinerId(), user.getFirstName(), file.getAbsolutePath());
		ObservableList<MessageVO> chatList = userChats.get(contact.getNinerID().trim());
		chatList.add(mess);
		chatString.scrollTo(chatList.size() - 1);
		presenter.sendFile(file, user.getNinerId(), ReceiverTypeEnum.INDIVIDUAL_MSG, contact.getNinerID());
	}

}