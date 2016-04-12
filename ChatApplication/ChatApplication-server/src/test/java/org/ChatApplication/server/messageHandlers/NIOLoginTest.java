/**
 * 
 */
package org.ChatApplication.server.messageHandlers;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.handlers.loginMessageHandler.ILoginMessageHandler;
import org.ChatApplication.server.receiver.NioServerModule;

/**
 * @author Devdatta
 *
 */
public class NIOLoginTest implements ILoginMessageHandler {

	User u = new User();
	
	
	public void validateLogin(User user, SelectionKey userKey) {
		// TODO Auto-generated method stub
		testData(user, userKey);
	}

	private void testData(User user, SelectionKey userKey) {
		user.getEmail();
	}

	public static void main(String[] args) {
		try {
			NioServerModule nioServerModule = NioServerModule.getNioServerModule();
			//SocketChannel sc = 
			//nioServerModule.addClient(sc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
