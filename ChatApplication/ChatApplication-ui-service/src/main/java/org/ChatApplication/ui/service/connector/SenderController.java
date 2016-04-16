package org.ChatApplication.ui.service.connector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
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
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
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
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
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

	public void signUp() {

	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket(HOST, PORT);
		SenderController s = new SenderController(socket);
		s.sendChatMessage("hello", "123456789", "123456745", ReceiverTypeEnum.INDIVIDUAL_MSG);

	}

	public void sendSearchContactString(String searchString, String senderId) {

		try {
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			List<ByteBuffer> buffArray = MessageUtility.packMessageToArray(searchString, senderId, "000000000",
					ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.SEARCH_USER);
			writeTodataOutputStream(buffArray);
		} catch (IOException e) {
			logger.error(e.getMessage());
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