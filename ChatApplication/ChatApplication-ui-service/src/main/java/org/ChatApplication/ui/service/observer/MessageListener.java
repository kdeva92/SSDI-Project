package org.ChatApplication.ui.service.observer;

import org.ChatApplication.ui.service.models.Message;
import org.ChatApplication.ui.service.utilities.ChatPage;

public class MessageListener {
	
	static MessageListener instance;
	ChatPage userInterface;
	
	public static MessageListener getInstance()
	{
		if(instance==null)
		{
			instance = new MessageListener();
		}
		
		return instance;
	}
	
	public void setUIInstance(ChatPage chatPage){
		this.userInterface = chatPage;
	}
	
	public void updateUI(String reciever, String message)
	{
		if(this.userInterface!=null)
			this.userInterface.dataT.add(new Message(reciever, "Anonymous", message));
	}

}
