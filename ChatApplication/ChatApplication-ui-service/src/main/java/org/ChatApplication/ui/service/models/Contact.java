package org.ChatApplication.ui.service.models;

import javafx.beans.property.SimpleStringProperty;

public class Contact {

	SimpleStringProperty ninerID;
	SimpleStringProperty name;
	SimpleStringProperty email;

	public Contact(String ninerID, String name, String email) {
		this.ninerID = new SimpleStringProperty(ninerID);
		this.name = new SimpleStringProperty(name);
		this.email = new SimpleStringProperty(email);
	}

	public Contact() {
		this.ninerID = null;
		this.name = null;
		this.email = null;
	}

	public String getNinerID() {
		return this.ninerID.get();
	}

	public void setNinerID(String sender) {
		this.ninerID.set(sender);
	}

	public String getName() {
		return this.name.get();
	}

	public void setName(String sender) {
		this.name.set(sender);
	}

	public String getEmail() {
		return this.email.get();
	}

	public void setEmail(String sender) {
		this.email.set(sender);
	}

	@Override
	public String toString() {
		return name.get();
	}
}
