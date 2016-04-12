package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.database.DatabaseConnecter;
import org.ChatApplication.ui.service.models.Contact;
import org.ChatApplication.ui.service.models.MessageVO;
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
	private CreateGroup createGroup;
	Connection conn;
	Statement stat;

	public Presenter(Homepage homepage) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.homePage = homepage;

		this.loginPage = new Login();
		this.chatPage = new ChatPage();
		this.contactsHandler = new ContactsHandler();
		this.createGroup = new CreateGroup();

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
			updateChatUI(message);
			break;
		case CREATE_GROUP:
			updateGroupCreation(message);
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

	private void updateGroupCreation(Message message) {
		
		//GroupVO group = ByteToEntityConverter.getInstance().getUser(message.getData());
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("INSERT INTO User VALUES('" + user.getNinerId() + "','" + user.getFirstName() + "','"
					+ user.getEmail() + "','" + "9999999999" + "','" + user.getPassword() + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void handleTermination() {

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
			this.user = ByteToEntityConverter.getInstance().getUser(message.getData());
			chatPage.loadChatPage(this, user);
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
		// senderController.searchContact(searchString);
	}

	/*
	 * Handle Add Contact
	 */

	public void addToContact(User user) {
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute("INSERT INTO User VALUES('" + user.getNinerId() + "','" + user.getFirstName() + "','"
					+ user.getEmail() + "','" + "9999999999" + "','" + user.getPassword() + "')");
			System.out.println("Contact Inserted to DB: " + user.getNinerId());
			chatPage.conT.add(new Contact(user.getNinerId(), user.getFirstName(), user.getEmail()));
			System.out.println("Contact Inserted UI: " + user.getNinerId());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendChatMessage(String senderId, String receiverId, String chatMessage,
			ReceiverTypeEnum receiverTypeEnum) {
		senderController.sendChatMessage(senderId, receiverId, chatMessage, receiverTypeEnum);
	}

	public void updateChatUI(Message message) {
		String messageBody = new String(message.getData());
		String receiver = new String(message.getReceiver());
		String name = getReceiverName(receiver);
		if (name != null) {
			MessageVO mess = new MessageVO(name, chatPage.user.getNinerId().trim(), messageBody);
			chatPage.dataT.add(mess);
		} else {
			MessageVO mess = new MessageVO(receiver, chatPage.user.getNinerId().trim(), messageBody);
			chatPage.dataT.add(mess);
		}
		DatabaseConnecter dbConnector = new DatabaseConnecter();
		conn = dbConnector.getConn();
		try {
			stat = conn.createStatement();
			stat.execute(
					"CREATE TABLE IF NOT EXISTS " + receiver + "Chat(sender varchar(10),messageBody varchar(500))");
			stat.execute("INSERT INTO " + receiver + "Chat VALUES('" + receiver + "','" + messageBody + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			rs = stat.executeQuery("SELECT studentName FROM User WHERE niner_id='" + ninerId + "'");

			while (rs.next()) {
				retVal = rs.getString(1);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;

	}
	
	
	
	public void loadCreateGroup(){
		try {
			createGroup.loadCreateGroupPage(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void loadChatPage(){
		try {
			this.chatPage.loadChatPage(this, this.user);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendCreateGroupMessage(GroupVO groupObject) {
		// TODO Auto-generated method stub
		senderController.createGroupMessage(this.user.getNinerId(), groupObject);
	}

	public User getUser() {
		return user;
	}

}
