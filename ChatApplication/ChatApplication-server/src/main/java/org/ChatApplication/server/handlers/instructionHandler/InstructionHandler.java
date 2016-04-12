/**
 * 
 */
package org.ChatApplication.server.handlers.instructionHandler;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.Group;
import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.service.UserService;
import org.ChatApplication.server.handlers.dataMessageHandler.DataMessageHandler;
import org.ChatApplication.server.handlers.messageHandler.MessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.server.sender.ClientData;
import org.ChatApplication.server.sender.ClientHolder;
import org.ChatApplication.server.sender.ISender;
import org.ChatApplication.server.sender.ServerSender;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.hibernate.mapping.Collection;

/**
 * @author Devdatta
 *
 */
public class InstructionHandler implements IInstructionHandler {

	ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final static Logger logger = Logger.getLogger(InstructionHandler.class);
	private HandlerThread handlerThread;
	private Thread thread;
	private static InstructionHandler instructionHandler;

	private InstructionHandler() {
		handlerThread = new HandlerThread();
		thread = new Thread(handlerThread);
		thread.start();
	}

	public static InstructionHandler getInstructionHandler() {
		if (instructionHandler != null)
			return instructionHandler;
		instructionHandler = new InstructionHandler();
		return instructionHandler;
	}

	public void handleMessage(Message message) {
		messageQueue.add(message);
		System.out.println("Instr hand added to queue");
	}

	private class HandlerThread implements Runnable {
		private UserService userService = UserService.getInstance();
		private ISender sender = ServerSender.getSender();
		ClientHolder clientHolder = ClientHolder.getClientHolder();
		ClientData cData;
		SocketChannel cSock;
		
		public void run() {
			while (true) {
				Message message = messageQueue.poll();
				if (message == null) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				switch (message.getType()) {
				case SEARCH_USER:
					List<User> users = null;
					try {
						users = userService.getUsers(new String(message.getData()));
						System.out.println("request str: " + message.getData() + " " + new String(message.getData()));
					} catch (Exception e) {
						e.printStackTrace();
					}

					try {
						message.setData(MessageUtility.packMessage(EntityToByteConverter.getInstance().getBytes(users),
								message.getReceiver(), message.getSender(), ReceiverTypeEnum.INDIVIDUAL_MSG,
								MessageTypeEnum.SEARCH_USER).array());
						System.out.println("search user reply data: " + new String(message.getData()));
					} catch (JsonGenerationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message.setData(null);
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message.setData(null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message.setData(null);
					}
					try {
						System.out.println("Sending to: ");
						
						cData = clientHolder.getClientData(message.getSender());
						cSock = cData.getSocketChannel();
						sender.sendMessage(cSock, message);
					} catch (NullPointerException e) {
						System.out.println("nullpointer!!");
						e.printStackTrace();
					}
					break;

				case CREATE_GROUP:
					try {
						GroupVO groupVO = ByteToEntityConverter.getInstance().getGroupVO(message.getData());
						List<User> usersForGrp = UserService.getInstance().getUsers(groupVO.getListOfMembers());
						Group group = new Group();
						group.setMembers(new HashSet<User>(usersForGrp));
						group.setName(groupVO.getGroupName());
						userService.createGroup(group);
						System.out.println("Group created: "+ groupVO.getGroupName()+" #members: "+group.getMembers().size());
						
						//notify each member about group
						Set<User> members = group.getMembers();
						for (Iterator iterator = members.iterator(); iterator.hasNext();) {
							User user = (User) iterator.next();
							cData = clientHolder.getClientData(user.getNinerId());
							cSock = cData.getSocketChannel();
							sender.sendMessage(cSock, message);
						}
						
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

				default:
					System.out.println("Default of inst handler");
					break;
				}

			}
		}

	}

}
