package org.ChatApplication.ui.service.models;

import javafx.beans.property.SimpleStringProperty;

public class MessageVO {

	SimpleStringProperty sender;
	SimpleStringProperty senderName;
	
	SimpleStringProperty messageBody;

	public MessageVO(String sender, String senderName, String messageBody) {
		this.sender = new SimpleStringProperty(sender);
		this.senderName = new SimpleStringProperty(senderName);
		this.messageBody = new SimpleStringProperty(messageBody);
	}

	public String getSender() {
		return this.sender.get();
	}

	public void setSender(String sender) {
		this.sender.set(sender);
	}

	public String getSenderName() {
		return this.senderName.get();
	}

	public void setSenderName(String senderName) {
		this.senderName.set(senderName);
	}

	public String getMessageBody() {
		return this.messageBody.get();
	}

	public void setMessageBody(String message) {
		this.messageBody.set(message);
	}
}
