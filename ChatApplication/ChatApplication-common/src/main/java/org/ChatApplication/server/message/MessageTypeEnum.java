package org.ChatApplication.server.message;

/**
 * 
 * @author Komal
 *
 */
public enum MessageTypeEnum {

	CHAT_MSG("Chat Message"), LOG_IN_MSG("Log in message"), LOG_OUT_MSG("Log out message"), EDIT_PROFILE(
			"Edit profile message"), ADD_CONTACT("Add contact message"), CREATE_GROUP(
					"Create group message"), EDIT_GROUP("Edit group message"), SEARCH_USER(
							"Search User message"), GET_USER("Get User message");

	private String description;

	private MessageTypeEnum(String description) {
		this.setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}