
/**
 * 
 */
package org.ChatApplication.server.handlers.dataMessageHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.Group;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.service.UserService;
import org.ChatApplication.server.handlers.messageHandler.MessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.ReceiverTypeEnum;
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
		thread = new Thread(handlerThread, "HandlerThread");
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
		System.out.println("Message: " + new String(message.getData()));
		messageQueue.add(message);
	}

	private class HandlerThread implements Runnable {

		private UserService userService = UserService.getInstance();
		private ISender sender = ServerSender.getSender();
		private ClientHolder clientHolder = ClientHolder.getClientHolder();
		private ClientData cData;
		private SocketChannel cSock;

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

				handleMessage(message);

				System.out.println("DataMessageHandler complete");

				// change when login implemented
				// ClientData clientData =
				// clientHolder.getClientData(message.getReceiver());
				// sender.sendMessage(clientData.getSocketChannel(), message);
				// Set<String> allClients =
				// clientHolder.getAllConnectedClients();
				// for (Iterator iterator = allClients.iterator();
				// iterator.hasNext();) {
				// String thisClient = (String) iterator.next();
				// sender.sendMessage(clientHolder.getClientData(thisClient).getSocketChannel(),
				// message);
				// // try {
				// // System.out.println("DataMessageHandler Broadcast "
				// // +
				// //
				// clientHolder.getClientData(thisClient).getSocketChannel().getRemoteAddress());
				// // } catch (IOException e) {
				// // // TODO Auto-generated catch block
				// // e.printStackTrace();
				// // }
				// }

			}
		}

		private void handleMessage(Message message) {
			// Handle the client not connected situation here..
			if (message.getReceiverType() == ReceiverTypeEnum.INDIVIDUAL_MSG.getIntEquivalant()) {
				System.out.println("DataMessageHandler sending to: " + message.getReceiver());
				// if (clientHolder.getClientData(message.getReceiver()) ==
				// null) {
				// System.out.println("Client not connected..");
				// continue;
				// }
				// //--working
				ByteBuffer byteBuffer = MessageUtility.packMessage(new String(message.getData()).trim().getBytes(),
						message.getSender(), message.getReceiver(), ReceiverTypeEnum.INDIVIDUAL_MSG, message.getType(),
						message.getPacketNo(), message.getNoOfPackets());
				sender.sendMessage(message.getReceiver(), byteBuffer.duplicate());

				// ByteBuffer b = ByteBuffer.wrap(message.getData());
				// System.out.println("asd");
				// b.flip();
				// sender.sendMessage(message.getReceiver(), b );
			} else if (message.getReceiverType() == ReceiverTypeEnum.GROUP_MSG.getIntEquivalant()) {
				// operate on group - DB access and individual send to each
				// receiver
				try {
					List<User> members = userService.getGroupMembers(Integer.parseInt(message.getReceiver()));

					ByteBuffer byteBuffer = MessageUtility.packMessage(new String(message.getData()).trim().getBytes(),
							message.getSender(), message.getReceiver(), ReceiverTypeEnum.GROUP_MSG, message.getType(),
							message.getPacketNo(), message.getNoOfPackets());

					for (Iterator iterator = members.iterator(); iterator.hasNext();) {
						User user = (User) iterator.next();
						if (user.getNinerId().equals(message.getSender()))
							continue;
						sender.sendMessage(user.getNinerId(), byteBuffer.duplicate());
					}

				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) throws Exception {
		UserService userService = UserService.getInstance();
		Group group = userService.getGroup(100000031);
		System.out.println(" " + group.getGroupId() + " users " + group.getMembers().size());
	}

}
