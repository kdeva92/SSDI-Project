package org.ChatApplication.ui.service.models;

import javafx.beans.property.SimpleStringProperty;

public class Message1 {

	SimpleStringProperty sender;
	SimpleStringProperty reciever;
	SimpleStringProperty messageBody;
	
	public Message1(String sender, String reciever, String messageBody)
	{
		this.sender = new SimpleStringProperty(sender);
		this.reciever = new SimpleStringProperty(reciever);
		this.messageBody = new SimpleStringProperty(messageBody);
	}
	
	public String getSender()
	{
		return this.sender.get();
	}
	
	public void setSender(String sender)
	{
		this.sender.set(sender);
	}
	
	public String getReciever()
	{
		return this.reciever.get();
	}
	
	public void setReciever(String reciever)
	{
		this.reciever.set(reciever);
	}
	
	public String getMessageBody()
	{
		return this.messageBody.get();
	}
	
	public void setMessageBody(String message)
	{
		this.messageBody.set(message);
	}
}
