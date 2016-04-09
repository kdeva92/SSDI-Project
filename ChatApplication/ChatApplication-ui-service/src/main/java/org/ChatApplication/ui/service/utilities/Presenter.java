package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.observer.MessageListener;
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
	Connection conn;
	Statement stat;

	public Presenter(Homepage homepage) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.homePage = homepage;

		this.loginPage = new Login();
		this.chatPage = new ChatPage();
		this.contactsHandler = new ContactsHandler();

		initializeClientDataBase();
	}

	private void initConnection() throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		this.messageListener = new MessageListener(this);
		this.serverController = new ServerController(this.messageListener);
		this.senderController = serverController.getSenderController();
		startListening();
	}

	private void initializeClientDataBase() {
		// TODO Auto-generated method stub
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		stat = null;
		try {
			stat = conn.createStatement();
			stat.execute(
					"CREATE TABLE IF NOT EXISTS User(niner_id varchar(10) primary key,studentName varchar(60),email varchar(60),contact varchar(10),password varchar(20))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.user = new User();
		this.user.setNinerId(userId);
		this.user.setPassword(password);

		this.senderController.logInMessage(this.user);

		// Message message= null;
		// while(true){
		// message = ChatApp.messageQueue.peek();
		// if(!message.equals(null)&&
		// !message.getType().equals(MessageTypeEnum.LOG_IN_MSG)){
		// message = ChatApp.messageQueue.poll();
		// try {
		// User user =
		// ByteToEntityConverter.getInstance().getUser(message.getData());
		// } catch (JsonParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// }
	}

	/*
	 * Login Response Handler
	 */
	public void handleUI(Message message) {
		switch (message.getType()) {
		case ADD_CONTACT:
			break;
		case CHAT_MSG:
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
			break;
		case TERMINATE:
			handleTermination();
			break;
		default:
			break;

		}

	}

	private void handleTermination() {

		try {
			serverController.terminateConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		loginPage.loadLoginPage(this);

		try {
			initConnection();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void handleLogIn(Message message) {
		// TODO Auto-generated method stub
		try {
			User user = ByteToEntityConverter.getInstance().getUser(message.getData());
			chatPage.loadChatPage(this);
		} catch (JsonParseException e) {
			loginPage.loadLoginPage(this);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			loginPage.loadLoginPage(this);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			loginPage.loadLoginPage(this);
			e.printStackTrace();
		}
	}

	/*
	 * Create Group
	 */

	public void createGrp(String groupName, ArrayList<User> userList) {

	}

	/*
	 * Search Contact
	 */
	public void searchContact(String searchString) {

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

}
