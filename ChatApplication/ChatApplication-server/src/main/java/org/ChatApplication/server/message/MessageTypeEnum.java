package org.ChatApplication.server.message;

import org.ChatApplication.server.handlers.dataMessageHandler.DataMessageHandler;
import org.ChatApplication.server.handlers.instructionHandler.InstructionHandler;
import org.ChatApplication.server.handlers.loginMessageHandler.LoginMessageHandler;

/**
 * 
 * @author Komal
 *
 */
public enum MessageTypeEnum {

	CHAT_MSG(DataMessageHandler.class), LOG_IN_MSG(LoginMessageHandler.class), LOG_OUT_MSG(
			InstructionHandler.class), EDIT_PROFILE(InstructionHandler.class), ADD_CONTACT(
					InstructionHandler.class), CREATE_GROUP(InstructionHandler.class), EDIT_GROUP(
							InstructionHandler.class), SEARCH_USER(InstructionHandler.class), GET_USER(
									InstructionHandler.class);

	private Class<?> handlerClass;

	public Class<?> getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(Class<?> handlerClass) {
		this.handlerClass = handlerClass;
	}

	MessageTypeEnum(Class<?> handlerClass) {
		this.handlerClass = handlerClass;
	}

}