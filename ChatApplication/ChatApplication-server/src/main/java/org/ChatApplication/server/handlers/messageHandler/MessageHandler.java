/**
 * 
 */
package org.ChatApplication.server.handlers.messageHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.server.handlers.dataMessageHandler.DataMessageHandler;
import org.ChatApplication.server.handlers.dataMessageHandler.IDataMessageHandler;
import org.ChatApplication.server.handlers.loginMessageHandler.ILoginMessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.sender.ClientHolder;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 */

public class MessageHandler implements IMessageHandler {

	ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final static Logger logger = Logger.getLogger(MessageHandler.class);
	private HandlerThread handlerThread;
	private Thread thread;

	private MessageHandler() {
		handlerThread = new HandlerThread();
		thread = new Thread(handlerThread);
		thread.start();
	}

	private static MessageHandler messageHandler;

	public static MessageHandler getMessageHandler() {
		if (messageHandler != null)
			return messageHandler;
		messageHandler = new MessageHandler();
		return messageHandler;
	}

	public void handleMessage(Message message) {
		// TODO Auto-generated method stub
		messageQueue.add(message);
	}

	/**
	 * 
	 * @author Devdatta
	 *
	 */
	/**
	 * 
	 * Class to handle queued messages. Messages are forwarded to corresponding
	 * handler
	 *
	 */
	private class HandlerThread implements Runnable {

		private IDataMessageHandler dataMessageHandler = DataMessageHandler.getDataMessageHandler();

		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				Message message = messageQueue.poll();
				if (message == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				System.out.println("MessageHandler msg:" + new String(message.getData().array()));
				dataMessageHandler.handleMessage(message);

				// switch (message.getType()) {
				// case CHAT_MSG:
				// System.out.println("Message type data");
				// dataMessageHandler.handleMessage(message);
				// break;
				//
				// default:
				// System.out.println("Message type default!!!");
				// dataMessageHandler.handleMessage(message);
				// break;
				// }

			}
		}

	}

}
