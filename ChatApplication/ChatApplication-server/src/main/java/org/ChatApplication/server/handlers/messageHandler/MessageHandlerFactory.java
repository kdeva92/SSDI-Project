package org.ChatApplication.server.handlers.messageHandler;

public class MessageHandlerFactory {

	private static MessageHandlerFactory messageHandlerFactory;

	private MessageHandlerFactory() {

	}

	public static MessageHandlerFactory getFactory() {
		if (messageHandlerFactory == null) {
			messageHandlerFactory = new MessageHandlerFactory();
		}
		return messageHandlerFactory;
	}

	// if property set to Test return other handler
	public IMessageHandler getMessageandler() {
		return MessageHandler.getMessageHandler();
	}

}
