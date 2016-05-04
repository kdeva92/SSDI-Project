/**
 * 
 */
package org.ChatApplication.ui.service.connector;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.ui.service.NonBufferedReceiverTest;
import org.ChatApplication.ui.service.application.ChatApp;
import org.ChatApplication.ui.service.observer.MessageListener;

/**
 * @author Devdatta
 *
 */
public class NonBufferedReceiver {

	InputStreamReader streamreader;
	public MessageListener messageListener;

	public NonBufferedReceiver(Socket socket, MessageListener ml) throws IOException {
		// TODO Auto-generated constructor stub
		System.out.println("In Non buffered recvr constrctor");
		this.messageListener = ml;
		streamreader = new InputStreamReader(socket.getInputStream());
		new Thread(new ReceiverPoller(), "ReceiverThread").start();
	}

	class ReceiverPoller implements Runnable {

		public void run() {
			byte[] data = new byte[Message.MAX_MESSAGE_SIZE];
			int size = 0;
			int read;
			boolean endZeroTrimFlag = false;
			while (true) {
				try {
					read = streamreader.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("Socket Closed");
					//System.exit(0);
					break;
				}
				if (read == 0 && endZeroTrimFlag)
					continue;
				if (read == -1) {
					Message terminationMessage = new Message();
					terminationMessage.setType(MessageTypeEnum.TERMINATE);
					ChatApp.messageQueue.add(terminationMessage);
					try {
						streamreader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}

				if (((byte) read) == Message.START_OF_MESSAGE) {
					data = new byte[Message.MAX_MESSAGE_SIZE];
					endZeroTrimFlag = false;
					data[size++] = (byte) read;
				} else if (read == Message.END_OF_MESSAGE) {
					size = 0;
					endZeroTrimFlag = true;
					try {
						Message message = MessageUtility.getMessage(ByteBuffer.wrap(data));
						System.out.println(new String(message.getData()));
						ChatApp.messageQueue.add(message);
						data = new byte[Message.MAX_MESSAGE_SIZE];

					} catch (Exception e) {
						e.printStackTrace();
					}
					// uncomment below for test
					// NonBufferedReceiverTest.showReceivedMessage(message);

				} else {
					data[size++] = (byte) read;
				}

			}
			System.out.println("Exiting While true");
		}
		

	}

}
