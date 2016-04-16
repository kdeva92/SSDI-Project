/**
 * 
 */
package org.ChatApplication.server.sender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ChatApplication.server.message.Message;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 */
public class ServerSender implements ISender {

	public static final int SENDER_EXECUTER_POOL_SIZE = 10;

	ConcurrentLinkedQueue<MessageData> messageQueue = new ConcurrentLinkedQueue<MessageData>();
	private final static Logger logger = Logger.getLogger(ServerSender.class);
	private SenderThread senderThread;
	private Thread thread;
	private ClientHolder clientHolder = ClientHolder.getClientHolder();
	
	private ServerSender() {
		// TODO Auto-generated constructor stub
		senderThread = new SenderThread();
		thread = new Thread(senderThread);
		thread.start();
	}

	private static ServerSender serverSender;

	public static ServerSender getSender() {
		if (serverSender != null)
			return serverSender;
		serverSender = new ServerSender();
		return serverSender;
	}

	public void sendMessage(String clientId, Message message) {

		//check if client is connected, if connected send message else add to database
		ClientData clientData = clientHolder.getClientData(clientId);
		if(clientData != null){
			//add to process queue
			messageQueue.add(new MessageData(message, clientData.getSocketChannel()));
		//System.out.println("ServerSender.. message added to queue");
		}
	}

	/**
	 * 
	 * Simple class to store message and socket channel suggested to use single
	 * instance
	 *
	 */
	private class MessageData {
		Message message;
		SocketChannel client;

		public MessageData(Message message, SocketChannel channel) {
			// TODO Auto-generated constructor stub
			this.message = message;
			client = channel;
		}

		public Message getMessage() {
			return message;
		}

		public void setMessage(Message message) {
			this.message = message;
		}

		public SocketChannel getClient() {
			return client;
		}

		public void setClient(SocketChannel client) {
			this.client = client;
		}
	}

	private class SenderThread implements Runnable {

		private ExecutorService executorService;

		public SenderThread() {
			// TODO Auto-generated constructor stub
			executorService = Executors.newFixedThreadPool(SENDER_EXECUTER_POOL_SIZE);
		}

		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				MessageData messageData = messageQueue.poll();
				if (messageData == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}

				// executor service
				// System.out.println("SenderThread.. Message sending to
				// executor service");
				executorService.execute(new Sender(messageData));
			}

		}

	}

	/**
	 * 
	 * Actual sender implementing runnable. This is target for executor service
	 * of SenderThread
	 *
	 */
	class Sender implements Runnable {

		MessageData messageData;

		public Sender(MessageData messageData) {
			// TODO Auto-generated constructor stub
			this.messageData = messageData;
			// System.out.println("Constructor of Sender - target of exec
			// serv");
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				// ObjectOutputStream oos = new
				// ObjectOutputStream(messageData.getClient().socket().getOutputStream());
				System.out.println("Server Sender Sending.. Client: " + messageData.getClient().getRemoteAddress()
						+ " Written object: " + new String(messageData.getMessage().getData()).trim());
				messageData.getClient().write(ByteBuffer.wrap(messageData.getMessage().getData()));
				//messageData.getClient().write((ByteBuffer.wrap(new Date().toString().getBytes())));
				
				// oos.writeObject(messageData.message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
