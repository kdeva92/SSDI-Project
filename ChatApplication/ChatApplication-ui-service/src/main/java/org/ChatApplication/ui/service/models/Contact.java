package org.ChatApplication.ui.service.models;

import javafx.beans.property.SimpleStringProperty;

public class Contact {

	SimpleStringProperty ninerID;
	SimpleStringProperty firstName;
	SimpleStringProperty email;
	SimpleStringProperty lastName;
	
	
	

	public Contact(String ninerID, String firstName, String lastName, String email) {
		this.ninerID = new SimpleStringProperty(ninerID);
		this.firstName = new SimpleStringProperty(firstName);
		this.email = new SimpleStringProperty(email);
		this.lastName = new SimpleStringProperty(lastName);
	}

	public Contact() {
		this.ninerID = null;
		this.firstName = null;
		this.lastName = lastName;
		this.email = null;
	}

	public String getNinerID() {
		return this.ninerID.get();
	}

	public void setNinerID(String sender) {
		this.ninerID.set(sender);
	}

	public String getLastName() {
		return this.lastName.get();
	}

	public void setLastName(String sender) {
		this.lastName.set(sender);
	}
	
	public String getFirstName() {
		return this.firstName.get();
	}

	public void setFirstName(String sender) {
		this.firstName.set(sender);
	}

	public String getEmail() {
		return this.email.get();
	}

	public void setEmail(String sender) {
		this.email.set(sender);
	}

	@Override
	public String toString() {
		return firstName.get();
	}
}
