/**
 * 
 */
package org.ChatServer.util;

import java.nio.ByteBuffer;

/**
 * @author Devdatta
 *
 */
public final class MessageUtility {

	//constants
	static int SENDER_SIZE =9;
	static int RECEIVER_SIZE =9;
	
	//private constructor 
	private MessageUtility(){}
	
	//update to return enum
	public static int getMessageType(ByteBuffer message) {	
		return message.get(1);
	}
	
	public static String getMessageReceiver(ByteBuffer message) {
		byte[] receiver = new byte[SENDER_SIZE];
		message.ge
		return new String();
	}
}
