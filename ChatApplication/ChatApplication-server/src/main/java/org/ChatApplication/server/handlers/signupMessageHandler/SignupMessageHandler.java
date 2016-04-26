package org.ChatApplication.server.handlers.signupMessageHandler;

import org.ChatApplication.server.message.Message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.converter.VoToEntitiyMapper;
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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * @author Devdatta
 *
 */

public class SignupMessageHandler implements ISignupMessageHandler {

	public static final String LOGIN_SUCCESS_RPLY = "success";
	ConcurrentLinkedQueue<UserDataHolder> messageQueue = new ConcurrentLinkedQueue<UserDataHolder>();
	private final static Logger logger = Logger.getLogger(SignupMessageHandler.class);
	private HandlerThread handlerThread;
	private Thread thread;

	private SignupMessageHandler() {
		handlerThread = new HandlerThread();
		thread = new Thread(handlerThread,"signupThread");
		thread.start();
	}

	private static SignupMessageHandler messageHandler;

	public static SignupMessageHandler getMessageHandler() {
		if (messageHandler != null)
			return messageHandler;
		messageHandler = new SignupMessageHandler();
		return messageHandler;
	}

	/*
	 * Validates the user and terminates the socket if invalid login request,
	 * else adds client to holder.
	 */

	public void doSignup(SelectionKey userKey, Message message) {
		messageQueue.add(new UserDataHolder(message, userKey));
		System.out.println("User added to signup queue "+messageQueue.size());
	}

	private class UserDataHolder {
		SelectionKey socketKey;
		Message message;

		public UserDataHolder(Message message, SelectionKey key) {
			this.message = message;
			socketKey = key;
		}

		public Message getMessage() {
			return message;
		}

		public SelectionKey getSocketKey() {
			return socketKey;
		}
	}

	private class HandlerThread implements Runnable {

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
				// Handle signup request

				Message message = dataHolder.getMessage();
				SocketChannel channel = (SocketChannel) dataHolder.getSocketKey().attachment();
				try {
					UserVO userVo = ByteToEntityConverter.getInstance().getUser(message.getData());
					User user = VoToEntitiyMapper.userToUserVo(userVo);

					try {
						userService.createUser(user);
						System.out.println("User Created");
					} catch (Exception e) {

						List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(e.getMessage(),
								message.getReceiver(), message.getSender(), ReceiverTypeEnum.INDIVIDUAL_MSG,
								MessageTypeEnum.SIGNUP);
						for (ByteBuffer buff : buffArray) {
							System.out.println("Sending to: ");
							serverSender.sendImmediateMessage(channel, buff);
						}

						System.out.println("create user reply data: " + e.getMessage());
						channel.close();
						dataHolder.getSocketKey().cancel();
						return;
					}

					List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(LOGIN_SUCCESS_RPLY,
							message.getReceiver(), message.getSender(), ReceiverTypeEnum.INDIVIDUAL_MSG,
							MessageTypeEnum.SIGNUP);
					for (ByteBuffer buff : buffArray) {
						System.out.println("Sending to: ");
						serverSender.sendImmediateMessage(channel, buff);
						System.out.println("create user reply data: " + new String(message.getData()));
					}
					channel.close();
					dataHolder.getSocketKey().cancel();
					continue;

				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
