/**
 * 
 */
package org.ChatApplication.server.handlers.loginMessageHandler;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.data.service.UserService;
import org.ChatApplication.server.handlers.dataMessageHandler.DataMessageHandler;
import org.ChatApplication.server.handlers.dataMessageHandler.IDataMessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.server.sender.ClientHolder;
import org.ChatApplication.server.sender.ServerSender;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 */
public class LoginMessageHandler implements ILoginMessageHandler {

	ConcurrentLinkedQueue<UserDataHolder> messageQueue = new ConcurrentLinkedQueue<UserDataHolder>();
	private final static Logger logger = Logger.getLogger(LoginMessageHandler.class);
	private HandlerThread handlerThread;
	private Thread thread;

	private LoginMessageHandler() {
		handlerThread = new HandlerThread();
		thread = new Thread(handlerThread);
		thread.start();
	}

	private static LoginMessageHandler messageHandler;

	public static LoginMessageHandler getMessageHandler() {
		if (messageHandler != null)
			return messageHandler;
		messageHandler = new LoginMessageHandler();
		return messageHandler;
	}

	// public User validateLogin(User user) {
	// // TODO Auto-generated method stub
	// try {
	// return UserService.getInstance().getUser(user.getNinerId(),
	// user.getPassword());
	// } catch (HibernateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// logger.error("Error in getting user from db", e);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// logger.error("Error in getting user from db", e);
	// }
	// return null;
	// }

	/*
	 * Validates the user and terminates the socket if invalid login request,
	 * else adds client to holder.
	 */
	public void validateLogin(User user, SelectionKey userKey) {
		messageQueue.add(new UserDataHolder(user, userKey));
		System.out.println("User added to login queue");
	}

	private class UserDataHolder {
		User user;
		SelectionKey socketKey;

		public UserDataHolder(User user, SelectionKey key) {
			this.user = user;
			socketKey = key;
		}

		public User getUser() {
			return user;
		}

		public SelectionKey getSocketKey() {
			return socketKey;
		}
	}

	private class HandlerThread implements Runnable {

		private IDataMessageHandler dataMessageHandler = DataMessageHandler.getDataMessageHandler();
		private ClientHolder clientHolder = ClientHolder.getClientHolder();
		private UserService userService = UserService.getInstance();
		ServerSender serverSender = ServerSender.getSender();

		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				UserDataHolder dataHolder = messageQueue.poll();
				if (dataHolder == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
				// Handle login request

				User user = dataHolder.getUser();
				try {
					User u = userService.getUser(user.getNinerId(), user.getPassword());
					if (u == null) {
						((SocketChannel) dataHolder.getSocketKey().attachment()).close();
						dataHolder.getSocketKey().cancel();
						continue;
					}
					// for valid login request
					clientHolder.addClient(user.getNinerId(), dataHolder.getSocketKey(),
							((SocketChannel) dataHolder.getSocketKey().attachment()));
					Message message = new Message();
					message.setType(MessageTypeEnum.LOG_IN_MSG);
					message.setReceiverType(ReceiverTypeEnum.INDIVIDUAL_MSG.getIntEquivalant());
					message.setSender("000000000");
					message.setReceiver(user.getNinerId());
					UserVO userVO = new UserVO();
					userVO.setEmail(u.getEmail());
					userVO.setFirstName(u.getFirstName());
					userVO.setId(u.getId());
					userVO.setLastName(u.getLastName());
					userVO.setNinerId(u.getNinerId());
					userVO.setPassword(null);
					ByteBuffer byteBuffer = MessageUtility.packMessage(
							EntityToByteConverter.getInstance().getBytes(userVO), "000000000", userVO.getNinerId(),
							ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.LOG_IN_MSG, 1, 1);
					serverSender.sendMessage(message.getReceiver(), byteBuffer);
					System.out.println("Login successful user added to client holder");
					// at this point send all pending messages to the client

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
