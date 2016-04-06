package org.ChatApplication.ui.service.connector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.apache.log4j.Logger;

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

			ByteBuffer buff = MessageUtility.packMessage(chatMessage.getBytes(), senderId, receiverId, receiverTypeEnum);
			byte[] b = buff.array();
			dataOutputStream.write(b, 0, b.length);
			System.out.println("written: " + new String(b, "UTF-8"));
			dataOutputStream.flush();
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

	public void logInMessage(User user) {
		try {
			byte[] user_byte = EntityToByteConverter.getInstance().getBytes(user);
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			ByteBuffer buff = MessageUtility.packMessage(user_byte, "000000000", "000000000", ReceiverTypeEnum.INDIVIDUAL_MSG);
			byte[] b = buff.array();

			dataOutputStream.write(b, 0, b.length);

			System.out.println("written: " + new String(b, "UTF-8"));
			dataOutputStream.flush();
			//System.out.println("Sent: " + userName);
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				dataOutputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

	public void signUp() {

	}

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket socket = new Socket(HOST, PORT);
		SenderController s = new SenderController(socket);
		s.sendChatMessage("hello", "123456789", "123456745", ReceiverTypeEnum.INDIVIDUAL_MSG);

	}

}
