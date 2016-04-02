/**
 * 
 */
package org.ChatApplication.ui.service;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.ui.service.connector.NonBufferedReceiver;

/**
 * @author Devdatta
 *
 */
public class NonBufferedReceiverTest {

	public static void showReceivedMessage(Message message) {
		System.out.println("Message received");
		System.out.println(new String(message.getData().array()).trim());
	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Trying to connect..");
		Socket socket = new Socket("127.0.0.1", 1515);
		System.out.println("Starting receiver");
		NonBufferedReceiver receiver = new NonBufferedReceiver(socket);
		System.out.println("Started receiver");

		ByteBuffer message = MessageUtility.packMessage("DemoMessage Hi ", "123456789", "987654321",
				ReceiverTypeEnum.INDIVIDUAL_MSG);
		System.out.println("Message before send: " + new String(message.array()).trim());
		socket.getOutputStream().write(message.array());
		socket.getOutputStream().flush();
		System.out.println("Message sent to server");

	}

}
