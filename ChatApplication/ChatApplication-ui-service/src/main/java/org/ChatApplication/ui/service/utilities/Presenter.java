package org.ChatApplication.ui.service.utilities;

import java.io.IOException;
import java.net.UnknownHostException;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.ui.service.connector.SenderController;
import org.ChatApplication.ui.service.connector.ServerController;
import org.ChatApplication.ui.service.observer.MessageListener;



public class Presenter {
	
private User user;	
private Homepage homePage;
private ChatPage chatPage;
private UserRegisteration signUpPage;
private Login loginPage;
private ServerController serverController;
private SenderController senderController;
private MessageListener messageListener;


	public Presenter(Homepage homepage) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.homePage = homepage;
		this.messageListener = new MessageListener(this);
		this.serverController = new ServerController(this.messageListener);
		this.senderController =  serverController.getSenderController();
		this.loginPage = new Login();
		this.chatPage = new ChatPage();
	}

	/*
	 * LoginPage loader
	 */
	public void loadLoginPage()
	{
		this.loginPage.loadLoginPage(this);
	}
	
	/*
	 * Login Message Sender
	 */
	public void sendLoginMessage(String userId,String password){
		this.user = new User();
		this.user.setNinerId(userId);
		this.user.setPassword(password);
		
		this.senderController.logInMessage(this.user);
	}
	
	/*
	 * Login Response Handler
	 */
	public void handleLogin(User user) throws IOException{
		if(user.equals(null))
		{
			this.loginPage.loadLoginPage(this);
		}
		else
		{
			this.chatPage.loadChatPage(this);
		}
	}
	
}
