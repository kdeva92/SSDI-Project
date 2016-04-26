/**
 * 
 */
package org.ChatApplication.server.handlers.instructionHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.converter.EntityToVoMapper;
import org.ChatApplication.common.converter.VoToEntitiyMapper;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.Group;
import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;
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

	private static final String LOGIN_SUCCESS_RPLY = "success";
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
						List<ByteBuffer> buffArray = MessageUtility
								.packMessageToArray(
										EntityToByteConverter.getInstance().getJsonString(
												EntityToVoMapper.userToUserVo(users)),
										message.getReceiver(), message.getSender(), ReceiverTypeEnum.INDIVIDUAL_MSG,
										MessageTypeEnum.SEARCH_USER);
						for (ByteBuffer buff : buffArray) {
							System.out.println("Sending to: ");
							sender.sendMessage(message.getSender(), buff);
							System.out.println("search user reply data: " + new String(message.getData()));

						}
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
						group.setMembers(usersForGrp);
						group.setName(groupVO.getGroupName());
						userService.createGroup(group);
						System.out.println(
								"Group created: " + groupVO.getGroupName() + " #members: " + group.getMembers().size());
						groupVO.setGroupId(group.getGroupId());

						List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(
								EntityToByteConverter.getInstance().getJsonString(groupVO), message.getSender(),
								message.getReceiver(), ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.CREATE_GROUP);
						for (ByteBuffer byteBuffer : buffArray) {
							List<User> members = group.getMembers();
							for (Iterator iterator = members.iterator(); iterator.hasNext();) {
								try {
									User user = (User) iterator.next();
									sender.sendMessage(user.getNinerId(), byteBuffer.duplicate());
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
							}
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
					break;

				

				case EDIT_GROUP:

					try {
						//get data from req
						GroupVO groupVO = ByteToEntityConverter.getInstance().getGroupVO(message.getData());
						List<User> usersForGrp = UserService.getInstance().getUsers(groupVO.getListOfMembers());
						
						// get existing group
						Group group = userService.getGroup(groupVO.getGroupId());
						List<User> emembers = group.getMembers();

						// update group
						group.setMembers(usersForGrp);
						group.setName(groupVO.getGroupName());
						group.setGroupId(groupVO.getGroupId());
						userService.updateGroup(group);
						System.out.println(
								"Group updated: " + groupVO.getGroupName() + " #members: " + group.getMembers().size());
						groupVO.setGroupId(group.getGroupId());

						List<User> members = group.getMembers();
						List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(
								EntityToByteConverter.getInstance().getJsonString(groupVO), message.getSender(),
								message.getReceiver(), ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.EDIT_GROUP);
						for (ByteBuffer byteBuffer : buffArray) {
							for (Iterator iterator = members.iterator(); iterator.hasNext();) {
								try {
									User user = (User) iterator.next();
									sender.sendMessage(user.getNinerId(), byteBuffer.duplicate());
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
							}
						}

						buffArray = MessageUtility.packMessageToArray(
								EntityToByteConverter.getInstance().getJsonString(groupVO), message.getSender(),
								message.getReceiver(), ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.GROUP_REMOVE);
						for (ByteBuffer byteBuffer : buffArray) {
							for (Iterator iterator = emembers.iterator(); iterator.hasNext();) {
								try {
									User user = (User) iterator.next();
									if(members.contains(user)){
										continue;
									}
									sender.sendMessage(user.getNinerId(), byteBuffer.duplicate());
								} catch (NullPointerException e) {
									e.printStackTrace();
								}
							}
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

					break;
				default:
					System.out.println("Default of inst handler");
					break;
				}

			}
		}

	}

	public static void main(String[] args) throws Exception {
		UserService userService = UserService.getInstance();
		Group group = userService.getGroup(100000000);
		for (int i = 0; i < 100000000; i++)
			userService.createGroup(group);
	}

}
