package org.ChatApplication.ui.service.utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.GroupTableObject;
import org.ChatApplication.ui.service.models.MessageVO;
import org.ChatApplication.ui.service.observer.MessageListener;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Presenter {

	private UserVO user;
	private Homepage homePage;
	private ChatPage chatPage;
	private Login loginPage;
	private ServerController serverController;
	private SenderController senderController;
	private MessageListener messageListener;
	private Connection conn;
	private Statement stat;
	private CreateGroup createGroup;
	private EditGroup editGroup;
	final static Logger logger = Logger.getLogger(Presenter.class);
	private Map<String, ArrayList<Message>> messageParts;

	public Presenter(Homepage homepage) throws UnknownHostException, IOException {
		this.homePage = homepage;
		this.loginPage = new Login();
		this.chatPage = new ChatPage();
		this.createGroup = new CreateGroup();
		this.editGroup = new EditGroup();

		messageParts = new HashMap<String, ArrayList<Message>>();
		// initializeClientDataBase();
	}

	private void initConnection() throws UnknownHostException, IOException {
		this.messageListener = new MessageListener(this);
		this.serverController = new ServerController(this.messageListener);
		this.senderController = serverController.getSenderController();
		startListening();
	}

	// private void initializeClientDataBase() {
	// DatabaseConnecter dbConnector = new DatabaseConnecter();
	// conn = dbConnector.getConn();
	// stat = null;
	// try {
	// stat = conn.createStatement();
	// stat.execute(
	// "CREATE TABLE IF NOT EXISTS User(niner_id varchar(10) primary
	// key,studentName varchar(60),email varchar(60),contact
	// varchar(10),password varchar(20))");
	// } catch (SQLException e) {
	// logger.error(e.getMessage());
	// }
	// }

	public void startListening() {
		messageListener.startListening();
	}

	/*
	 * LoginPage loader
	 */
	public void loadLoginPage() {
		this.loginPage.loadLoginPage(this);
	}

	/*
	 * Login Message Sender
	 */
	public void sendLoginMessage(String userId, String password) {
		try {
			initConnection();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		this.user = new UserVO();
		this.user.setNinerId(userId);
		this.user.setPassword(password);

		this.senderController.logInMessage(this.user);

	}

	/*
	 * Login Response Handler
	 */
	public void handleUI(Message message) {
		if (message != null && message.getType() != null) {
			System.out.println("Before switch case");
			switch (message.getType()) {
			case ADD_CONTACT:
				break;
			case CHAT_MSG:
				handleChatMessage(message);
				break;
			case CREATE_GROUP:
				updateGroupCreation(message);
				break;
			case EDIT_GROUP:
				updateEditGroup(message);
				break;
			case EDIT_PROFILE:
				break;
			case GET_USER:
				break;
			case LOG_IN_MSG:
				handleLogIn(message);
				break;
			case LOG_OUT_MSG:
				break;
			case SEARCH_USER:
				handleSearchUser(message);
				break;
			case TERMINATE:
				handleTermination();
				break;
			case SIGNUP:
				handleSignUP(message);
				System.out.println("At Handle Signup");
				break;
			case FILE_MSG:
				handleFileMessage(message);
				break;
			default:
				break;

			}
		}

	}

	private void handleFileMessage(Message message) {
		handleChatMessage(message);

	}

	private void handleSignUP(Message message) {
		try {
			serverController.terminateConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// loadHomepage();
		String status1 = new String(message.getData());
		if (status1.equals("success")) {
			System.out.println("Signup Successful");
			Alerts.createInformationAlert("Signup is Successful", null, null);
		} else {
			System.out.println("Signup Unsuccessful !!!!!!!!!!!!!");
			Alerts.createInformationAlert(status1, null, null);
		}

	}

	private void handleSearchUser(Message message) {

		try {
			List<UserVO> users = ByteToEntityConverter.getInstance().getUsers(message.getData());
			chatPage.renderSearchAlert(users);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public void handleTermination() {

		try {
			serverController.terminateConnection();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		// loginPage.loadLoginPage(this);
		loadHomepage();

		try {
			initConnection();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private void handleLogIn(Message message) {
		try {
			this.user = ByteToEntityConverter.getInstance().getUser(message.getData());
			chatPage.loadChatPage(this, user);
		} catch (JsonParseException e) {
			loginPage.loadLoginPage(this);
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			loginPage.loadLoginPage(this);
			logger.error(e.getMessage());
		} catch (IOException e) {
			loginPage.loadLoginPage(this);
			logger.error(e.getMessage());
		}
	}

	/*
	 * Handle Add Contact
	 */

	public void addToContact(UserVO recieved_user) {
		if (user.getNinerId().equals(recieved_user.getNinerId())) {
			Alerts.createInformationAlert("Cannot add yourself", null, null);
		} else {
			DatabaseConnecter dbConnector = new DatabaseConnecter();
			conn = dbConnector.getConn();
			try {
				stat = conn.createStatement();
				ResultSet rs = stat.executeQuery("SELECT niner_id FROM Contacts_" + user.getNinerId()
						+ " WHERE niner_id='" + recieved_user.getNinerId() + "'");
				int flag = 0;
				while (rs.next()) {
					flag++;
				}
				if (flag == 0) {
					stat.execute("INSERT INTO Contacts_" + user.getNinerId() + " VALUES('" + recieved_user.getNinerId()
							+ "','" + recieved_user.getFirstName() + "','" + recieved_user.getLastName() + "','"
							+ recieved_user.getEmail() + "')");
					System.out.println("Contact Inserted to DB: " + user.getNinerId());

					ObservableList<MessageVO> newContactChat = FXCollections.observableArrayList();
					this.chatPage.userChats.put(recieved_user.getNinerId(), newContactChat);
					chatPage.conT.add(new Contact(recieved_user.getNinerId(), recieved_user.getFirstName(),
							recieved_user.getLastName(), recieved_user.getEmail()));
					System.out.println("Contact Inserted UI: " + user.getNinerId());
					stat.execute(
							"CREATE TABLE IF NOT EXISTS Chat_" + user.getNinerId() + "_" + recieved_user.getNinerId()
									+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
					System.out.println("Table created as:\nCREATE TABLE IF NOT EXISTS Chat_" + user.getNinerId() + "_"
							+ recieved_user.getNinerId()
							+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handle Search contact
	 * 
	 * @param searchString
	 */

	public void searchContact(String searchString) {
		this.chatPage.searchUserT.clear();
		senderController.sendSearchContactString(searchString, user.getNinerId());

	}

	public void sendChatMessage(String senderId, String receiverId, String chatMessage,
			ReceiverTypeEnum receiverTypeEnum) {
		senderController.sendChatMessage(senderId, receiverId, chatMessage, receiverTypeEnum);
	}

	public void handleChatMessage(Message message) {
		String messageBody = "";
		String receiver = new String(message.getSender());
		String receiverName = receiver;
		for (Contact con : this.chatPage.conT) {
			if (con.getNinerID().equals(receiver))
				receiverName = con.getFirstName();
		}

		if (message.getNoOfPackets() > 1) {
			if (messageParts.containsKey(message.getSender())) {
				ArrayList<Message> arrayList = messageParts.get(message.getSender());
				arrayList.add(message);
				if (message.getNoOfPackets() == arrayList.size()) {
					arrayList.sort(new Comparator<Message>() {

						public int compare(Message o1, Message o2) {

							return o1.getPacketNo() - o2.getPacketNo();
						}
					});

					if (message.getType() == MessageTypeEnum.CHAT_MSG) {
						for (Message m : arrayList) {
							messageBody += new String(m.getData());
						}
					} else {
						messageBody = new String(arrayList.get(0).getData());
						try {
							// File file= new File("C:"+ File.separator +
							// messageBody);
							String filePath = System.getProperty("user.home") + File.separator + File.separator
									+ messageBody;
							FileOutputStream stream = new FileOutputStream(filePath);
							for (int i = 1; i < arrayList.size(); i++) {

								stream.write(arrayList.get(i).getData());
							}
							stream.close();
							
							Alert alert = Alerts.createFileKeepAlert();
							ButtonType buttonYes = new ButtonType("Yes");
							ButtonType buttonNo = new ButtonType("No");
							alert.getButtonTypes().setAll(buttonYes, buttonNo);
							Optional<ButtonType> result = alert.showAndWait();
							if (result.get() == buttonYes) {
								File file = new File(filePath);
								Desktop.getDesktop().open(file);
							} else {
								alert.close();
							}
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					messageParts.remove(message.getSender());
				}
			} else {
				ArrayList<Message> messageArray = new ArrayList<Message>();
				messageArray.add(message);
				messageParts.put(message.getSender(), messageArray);
			}
		} else {
			messageBody = new String(message.getData());
		}

		// String group = new String(message.getReceiver());

		switch (message.getReceiverType()) {

		case 0:// Individual Message
			if (!messageBody.isEmpty()) {
				addMessageToDB(receiver, receiverName, messageBody);
				ObservableList<MessageVO> chatArray = this.chatPage.userChats.get(receiver);
				chatArray.add(new MessageVO(receiver, receiverName, messageBody));
				this.chatPage.chatString.scrollTo(chatArray.size() - 1);
			}
			break;

		case 1:// Group Message
			if (!messageBody.isEmpty()) {
				addGroupMessageToDB(message.getReceiver(), receiver, receiverName, messageBody);
				ObservableList<MessageVO> chatArray = this.chatPage.userChats.get(message.getReceiver());
				chatArray.add(new MessageVO(receiver, receiverName, messageBody));
				this.chatPage.chatString.scrollTo(chatArray.size() - 1);

			}
			break;
		}

	}

	private void addMessageToDB(String receiver, String receiverName, String messageBody) {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS Chat_" + user.getNinerId() + "_" + receiver
					+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
			for (Contact contact : this.chatPage.conT) {
				if (contact.getNinerID().equals(receiver))
					stat.execute("INSERT INTO Chat_" + user.getNinerId() + "_" + receiver + " VALUES('" + receiver
							+ "','" + receiverName + "','" + messageBody + "')");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addGroupMessageToDB(String groupID, String receiver, String receiverName, String messageBody) {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("CREATE TABLE IF NOT EXISTS Grp_" + user.getNinerId() + "_" + groupID
					+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
			for (GroupTableObject group : this.chatPage.groupT) {
				if ((group.getGroupID() + "").equals(groupID))
					stat.execute("INSERT INTO Grp_" + user.getNinerId() + "_" + groupID + " VALUES('" + receiver + "','"
							+ receiverName + "','" + messageBody.trim() + "')");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateGroupCreation(Message message) {

		String membersList = null;
		GroupVO group = null;
		try {
			group = ByteToEntityConverter.getInstance().getGroupVO(message.getData());

			membersList = "";
			for (String member : group.getListOfMembers()) {
				if (membersList.equals("")) {
					membersList += member.trim();
				} else {
					membersList += "," + member.trim();
				}
			}

		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("INSERT INTO Groups_" + user.getNinerId() + " VALUES(" + group.getGroupId() + ",'"
					+ group.getGroupName() + "','" + membersList + "')");
			System.out.println("INSERT INTO Groups_" + user.getNinerId() + " VALUES(" + group.getGroupId() + ",'"
					+ group.getGroupName() + "','" + membersList + "')");
			stat.execute("CREATE TABLE IF NOT EXISTS Grp_" + user.getNinerId() + "_" + group.getGroupId()
					+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
			System.out.println("CREATE TABLE IF NOT EXISTS Grp_" + user.getNinerId() + "_" + group.getGroupId()
					+ "(sender varchar(10),senderName varchar(50),messageBody varchar(500))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadChatPage();

	}

	public void loadCreateGroup() {
		try {
			createGroup.loadCreateGroupPage(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadChatPage() {
		try {
			this.chatPage.loadChatPage(this, this.user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadHomepage() {
		this.homePage.loadHomepage(this);
	}

	public void sendCreateGroupMessage(GroupVO groupObject) {
		senderController.createGroupMessage(this.user.getNinerId(), groupObject);
		// loadChatPage();
	}

	public UserVO getUser() {
		return user;
	}

	public void setUser(UserVO user) {
		this.user = user;
	}

	private String getReceiverName(String ninerId) {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet rs;
		String retVal = null;
		try {
			rs = stat.executeQuery("SELECT first_name FROM Contacts_" + user.getNinerId() + " WHERE niner_id='"
					+ ninerId.trim() + "'");

			while (rs.next()) {
				retVal = rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;

	}

	public void loadEditGroup(GroupTableObject group, ObservableList<Contact> conT) {
		try {
			this.editGroup.loadEditGroup(group, this, conT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendSignUpMessage(UserVO userVO) {

		try {

			initConnection();
			System.out.println("Connection Initialized");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		senderController.signUpMessage(userVO.getNinerId(), userVO);
		loadHomepage();
	}

	public void sendEditGroupMessage(GroupVO groupVO) {
		senderController.editGroupMessage(this.user.getNinerId(), groupVO);

	}

	private void updateEditGroup(Message message) {

		String membersList = null;
		GroupVO group = null;
		try {
			group = ByteToEntityConverter.getInstance().getGroupVO(message.getData());

			membersList = "";
			for (String member : group.getListOfMembers()) {
				if (membersList.equals("")) {
					membersList += member.trim();
				} else {
					membersList += "," + member.trim();
				}
			}

		} catch (JsonParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Group Edited as : " + group.getGroupId() + " " + group.getGroupName() + " " + membersList);
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("UPDATE Groups_" + this.getUser().getNinerId() + " SET group_name='"
					+ group.getGroupName().trim() + "' WHERE group_id=" + group.getGroupId());
			stat.execute("UPDATE Groups_" + this.getUser().getNinerId() + " SET members='" + membersList
					+ "' WHERE group_id=" + group.getGroupId());

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.chatPage.conT.clear();
		loadChatPage();

	}

	public void sendFile(File file, String senderId, ReceiverTypeEnum receiverTypeEnum, String receiverId) {
		senderController.sendFile(file, senderId, receiverTypeEnum, receiverId);

	}
	
	public void terminateSocket(){
		try {
			serverController.terminateConnection();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
