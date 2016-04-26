package org.ChatApplication.server.handlers.signupMessageHandler;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.ChatApplication.server.handlers.messageHandler.IMessageHandler;
import org.ChatApplication.server.message.Message;

public interface ISignupMessageHandler {

	public void doSignup(SelectionKey userKey, Message message);
}
