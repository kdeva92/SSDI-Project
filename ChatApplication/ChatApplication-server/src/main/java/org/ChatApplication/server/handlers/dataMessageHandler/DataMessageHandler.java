/**
 * 
 */
package org.ChatApplication.server.handlers.dataMessageHandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.server.handlers.messageHandler.MessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.sender.ClientData;
import org.ChatApplication.server.sender.ClientHolder;
import org.ChatApplication.server.sender.ISender;
import org.ChatApplication.server.sender.ServerSender;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 */
public class DataMessageHandler implements IDataMessageHandler {

	private static DataMessageHandler dataMessageHandler;

	ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final static Logger logger = Logger.getLogger(MessageHandler.class);
	private HandlerThread handlerThread;
	private Thread thread;

	private DataMessageHandler() {
		handlerThread = new HandlerThread();
		thread = new Thread(handlerThread);
		thread.start();
	}

	public static DataMessageHandler getDataMessageHandler() {
		if (dataMessageHandler != null)
			return dataMessageHandler;
		dataMessageHandler = new DataMessageHandler();
		return dataMessageHandler;

	}

	public void handleMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("Message: " + new String(message.getData().array()));
		messageQueue.add(message);
	}

	private class HandlerThread implements Runnable {

		private ISender sender = ServerSender.getSender();
		private ClientHolder clientHolder = ClientHolder.getClientHolder();

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

				// change when login implemented
				// ClientData clientData =
				// clientHolder.getClientData(message.getReceiver());
				// sender.sendMessage(clientData.getSocketChannel(), message);

				Set<String> allClients = clientHolder.getAllConnectedClients();
				System.out.println("DataMessageHandler Broadcast to all connected clients.." + allClients.size());
				for (Iterator iterator = allClients.iterator(); iterator.hasNext();) {
					String thisClient = (String) iterator.next();
					sender.sendMessage(clientHolder.getClientData(thisClient).getSocketChannel(), message);
					try {
						System.out.println("DataMessageHandler Broadcast "
								+ clientHolder.getClientData(thisClient).getSocketChannel().getRemoteAddress());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

	}

}
