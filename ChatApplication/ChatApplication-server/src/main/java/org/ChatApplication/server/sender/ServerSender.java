/**
 * 
 */
package org.ChatApplication.server.sender;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.apache.log4j.Logger;

import lightweightDatabase.LightweightDatabaseManager;

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

	public void sendOfflineQueueMessages(String client) {
		try {
			List<ByteBuffer> msgs = LightweightDatabaseManager.getAllMessagesForUser(client);
			if (msgs != null) {
				for (Iterator iterator = msgs.iterator(); iterator.hasNext();) {
					ByteBuffer byteBuffer = (ByteBuffer) iterator.next();
					// byteBuffer.flip();
					Message m = MessageUtility.getMessage(byteBuffer);
					byteBuffer = MessageUtility.packMessage(m.getData(), m.getSender(), m.getReceiver(),
							ReceiverTypeEnum.getReceiverTypeEnumByIntValue(m.getReceiverType()), m.getType(),
							m.getPacketNo(), m.getNoOfPackets());
					sendMessage(client, byteBuffer);
					System.out.println("processing offline msg: " + new String(byteBuffer.array()).trim());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Failed to store to lightweight database", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to store to lightweight database", e);
		}
	}

	public boolean sendImmediateMessage(SocketChannel channel, ByteBuffer message) {

		try {
			System.out.println("Server Sender Sending.. Client: " + channel.getLocalAddress() + " Written object: "
					+ new String(message.array()).trim());
			message.flip();
			channel.write(message);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void sendMessage(String clientId, ByteBuffer byteBuffer) {

		// check if client is connected, if connected send message else add to
		// database
		ClientData clientData = clientHolder.getClientData(clientId);
		if (clientData != null) {
			// add to process queue
			byteBuffer.flip();
			messageQueue.add(new MessageData(byteBuffer, clientData.getSocketChannel()));
			// System.out.println("ServerSender.. message added to queue");
		} else {
			try {
				LightweightDatabaseManager.storeMessage(clientId, byteBuffer);
				System.out.println("message added to offline queue: " + new String(byteBuffer.array()).trim());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("Failed to store to lightweight database", e);
			}
		}
	}

	/**
	 * 
	 * Simple class to store message and socket channel suggested to use single
	 * instance
	 *
	 */
	private class MessageData {
		ByteBuffer byteBuffer;
		SocketChannel client;

		public MessageData(ByteBuffer byteBuffer, SocketChannel channel) {
			this.byteBuffer = byteBuffer;
			client = channel;
		}

		public ByteBuffer getByteBuffer() {
			return byteBuffer;
		}

		public void setByteBuffer(ByteBuffer byteBuffer) {
			this.byteBuffer = byteBuffer;
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
						+ " Written object: " + new String(messageData.getByteBuffer().array()).trim());
				messageData.getClient().write(messageData.getByteBuffer());
				// messageData.getClient().close();
				// messageData.getClient().write((ByteBuffer.wrap(new
				// Date().toString().getBytes())));

				// oos.writeObject(messageData.message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
