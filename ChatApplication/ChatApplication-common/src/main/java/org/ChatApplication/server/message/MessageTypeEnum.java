package org.ChatApplication.server.message;

import java.util.HashMap;

/**
 * 
 * @author Komal
 *
 */
public enum MessageTypeEnum {

	CHAT_MSG("Chat Message", 1), LOG_IN_MSG("Log in message", 2), LOG_OUT_MSG("Log out message", 3), EDIT_PROFILE(
			"Edit profile message", 4), ADD_CONTACT("Add contact message", 5), CREATE_GROUP("Create group message",
					6), EDIT_GROUP("Edit group message", 7), SEARCH_USER("Search User message",
							8), GET_USER("Get User message", 9), TERMINATE("Termination message", 10);

	private static HashMap<Integer, MessageTypeEnum> valueMap = new HashMap<Integer, MessageTypeEnum>();

	static {
		for (MessageTypeEnum e : MessageTypeEnum.values()) {
			valueMap.put(e.getIntEquivalant(), e);
		}
	}

	private String description;
	private int intEquivalant;

	private MessageTypeEnum(String description, int value) {
		this.description = description;
		this.intEquivalant = value;
	}

	public String getDescription() {
		return description;
	}

	public byte getByteEquivalant() {
		return (byte) intEquivalant;
	}

	public int getIntEquivalant() {
		return intEquivalant;
	}

	public static MessageTypeEnum getMessageTypeEnumByIntValue(int value) {
		return valueMap.get(value);
	}
}
