package org.ChatApplication.ui.service.connector;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

/**
 * 
 * @author Komal
 *
 */
public class SenderController {

	private Socket socket;
	private DataOutputStream dataOutputStream;
	private static String HOST = "127.0.0.1";
	private static int PORT = 1515;

	private Logger logger = Logger.getLogger(getClass());

	public SenderController(Socket socket) {
		this.socket = socket;
		try {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendChatMessage(String senderId, String receiverId, String chatMessage,
			ReceiverTypeEnum receiverTypeEnum) {
		try {

			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(chatMessage, senderId, receiverId,
					receiverTypeEnum, MessageTypeEnum.CHAT_MSG);
			writeTodataOutputStream(buffArray);
			if (logger.isDebugEnabled()) {
				logger.debug("Sent: " + chatMessage);
			}

		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			// try {
			// dataOutputStream.close();
			// } catch (IOException e) {
			// logger.error(e.getMessage());
			// }

		}

	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @param senderId
	 */

	public void logInMessage(UserVO user) {
		try {
			String userStr = EntityToByteConverter.getInstance().getJsonString(user);
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(userStr, user.getNinerId(), "000000000",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.LOG_IN_MSG);
			writeTodataOutputStream(buffArray);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public void createGroupMessage(String sender, GroupVO groupObject) {
		try {
			String groupStr = EntityToByteConverter.getInstance().getJsonString(groupObject);
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(groupStr, sender, "000000000",
					ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.CREATE_GROUP);
			writeTodataOutputStream(buffArray);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void editGroupMessage(String sender, GroupVO groupObject) {
		try {
			String groupStr = EntityToByteConverter.getInstance().getJsonString(groupObject);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(groupStr, sender, "000000000",
					ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.EDIT_GROUP);
			writeTodataOutputStream(buffArray);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void signUpMessage(String sender,UserVO user) {
		try {
			String userStr = EntityToByteConverter.getInstance().getJsonString(user);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(userStr, sender, "000000000",
					ReceiverTypeEnum.GROUP_MSG, MessageTypeEnum.SIGNUP);
			writeTodataOutputStream(buffArray);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void signUp() {

	}

	public void sendSearchContactString(String searchString, String senderId) {

		try {
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(searchString, senderId, "000000000",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.SEARCH_USER);
			writeTodataOutputStream(buffArray);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	public void sendFile(File file, String senderId, ReceiverTypeEnum receiverTypeEnum, String receiverId) {
		try {
			Path path = Paths.get(file.getAbsolutePath());
			byte[] bytes = Files.readAllBytes(path);

			List<ByteBuffer> buffArray = new ArrayList<ByteBuffer>();
			int size = bytes.length / MessageUtility.CHUNK_SIZE + 1;
			FileInputStream in = new FileInputStream(file);
			for (int i = 0; i < size; i++) {
				byte[] data = new byte[MessageUtility.CHUNK_SIZE];
				int bytesAct = in.read(data, 0, MessageUtility.CHUNK_SIZE);
				if (bytesAct != MessageUtility.CHUNK_SIZE) { // to make sure
																// there is no
																// empty spaces
					byte[] toReturn = new byte[bytesAct];
					for (int j = 0; j < toReturn.length; j++) {
						toReturn[j] = data[j];
					}
					buffArray.add(MessageUtility.packMessage(toReturn, senderId, receiverId, receiverTypeEnum,
							MessageTypeEnum.FILE_MSG, i + 1, size + 1));
				} else {
					buffArray.add(MessageUtility.packMessage(data, senderId, receiverId, receiverTypeEnum,
							MessageTypeEnum.FILE_MSG, i + 1, size + 1));
				}
			}

			buffArray.add(MessageUtility.packMessage(file.getName().getBytes(), senderId, receiverId, receiverTypeEnum,
					MessageTypeEnum.FILE_MSG, 0, size + 1));

			writeTodataOutputStream(buffArray);
//			in.close();
		} catch (IOException e) {
				return;
		}
	}

	private void writeTodataOutputStream(List<ByteBuffer> buffArray) throws IOException {
		for (ByteBuffer buff : buffArray) {
			byte[] b = buff.array();
			dataOutputStream.write(b, 0, b.length);
			System.out.println("written: " + new String(b, "UTF-8"));
			dataOutputStream.flush();
		}
	}

}