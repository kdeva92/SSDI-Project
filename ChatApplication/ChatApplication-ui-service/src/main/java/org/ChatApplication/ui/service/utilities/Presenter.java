package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Message1;
import org.ChatApplication.ui.service.observer.MessageListener;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

public class Presenter {

	private User user;
	private Homepage homePage;
	private ChatPage chatPage;
	private UserRegisteration signUpPage;
	private Login loginPage;
	private ServerController serverController;
	private SenderController senderController;
	private MessageListener messageListener;
	private ContactsHandler contactsHandler;
	private Connection conn;
	private Statement stat;
	final static Logger logger = Logger.getLogger(Presenter.class);

	public Presenter(Homepage homepage) throws UnknownHostException, IOException {
		this.homePage = homepage;
		this.loginPage = new Login();
		this.chatPage = new ChatPage();
		this.contactsHandler = new ContactsHandler();

		initializeClientDataBase();
	}

	private void initConnection() throws UnknownHostException, IOException {
		this.messageListener = new MessageListener(this);
		this.serverController = new ServerController(this.messageListener);
		this.senderController = serverController.getSenderController();
		startListening();
	}

	private void initializeClientDataBase() {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		stat = null;
		try {
			stat = conn.createStatement();
			stat.execute(
					"CREATE TABLE IF NOT EXISTS User(niner_id varchar(10) primary key,studentName varchar(60),email varchar(60),contact varchar(10),password varchar(20))");
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

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
		this.user = new User();
		this.user.setNinerId(userId);
		this.user.setPassword(password);

		this.senderController.logInMessage(this.user);

	}

	/*
	 * Login Response Handler
	 */
	public void handleUI(Message message) {
		if (message != null && message.getType() != null) {
			switch (message.getType()) {
			case ADD_CONTACT:
				break;
			case CHAT_MSG:
				updateChatUI(message);
				break;
			case CREATE_GROUP:
				break;
			case EDIT_GROUP:
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
			default:
				break;

			}
		}

	}

	private void handleSearchUser(Message message) {

		try {
			List<User> users = ByteToEntityConverter.getInstance().getUsers(message.getData());
			chatPage.renderSearchAlert(users);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	private void handleTermination() {

		try {
			serverController.terminateConnection();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		loginPage.loadLoginPage(this);

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
	 * Create Group
	 */

	public void createGrp(String groupName, ArrayList<User> userList) {

	}
	/*
	 * Handle Add Contact
	 */

	public void addToContact(User user) {
		if (user.equals(null)) {
			this.contactsHandler.add2Contacts(user, conn);
		} else {

		}
	}

	/**
	 * Handle Search contact
	 * 
	 * @param searchString
	 */

	public void searchContact(String searchString) {
		senderController.sendSearchContactString(searchString, user.getNinerId());

	}

	public void sendChatMessage(String senderId, String receiverId, String chatMessage,
			ReceiverTypeEnum receiverTypeEnum) {
		senderController.sendChatMessage(senderId, receiverId, chatMessage, receiverTypeEnum);
	}

	public void updateChatUI(Message message) {
		String messageBody = new String(message.getData());
		String receiver = new String(message.getReceiver());
		Message1 mess = new Message1(messageBody, chatPage.user.getNinerId().trim(), messageBody);
		chatPage.dataT.add(mess);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
