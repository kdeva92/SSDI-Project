/**
 * 
 */
package org.ChatApplication.ui.service;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;

/**
 * @author Devdatta
 *
 */
public class NonBufferedReceiverTest {

	public static void showReceivedMessage(Message message) {
		System.out.println("Message received");
		System.out.println(new String(message.getData()).trim());
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Trying to connect..");
		Socket socket = new Socket("192.168.82.1", 1515);
		System.out.println("Starting receiver");
		// NonBufferedReceiver receiver = new NonBufferedReceiver(socket);
		System.out.println("Started receiver");

		List<ByteBuffer> message = MessageUtility.packMessageToArray("DemoMessage Hi ", "123456789", "987654321",
				ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG);
		System.out.println("Message before send: " + new String(message.get(0).array()).trim());
		socket.getOutputStream().write(message.get(0).array());
		socket.getOutputStream().flush();
		System.out.println("Message sent to server");

	}

}
