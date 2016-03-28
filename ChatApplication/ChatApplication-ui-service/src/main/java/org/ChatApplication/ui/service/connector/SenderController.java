package org.ChatApplication.ui.service.connector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.ChatApplication.ui.service.models.ReceiverTypeEnum;
import org.ChatApplication.ui.service.utilities.MessageUtility;
import org.ChatApplication.ui.service.utilities.PropertyReader;
import org.apache.log4j.Logger;

/**
 * 
 * @author Komal
 *
 */
public class SenderController {

	private Socket socket;
	private DataOutputStream dos;
	private static SenderController instance;
	private static String HOST = "127.0.0.1";
	private static int PORT = 1515;

	private Logger logger = Logger.getLogger(getClass());

	public static SenderController getInstance() {
		if (instance == null) {
			instance = new SenderController();
		}
		return instance;
	}

	public void sendChatMessage(String senderId, String receiverId, String chatMessage,
			ReceiverTypeEnum receiverTypeEnum) {
		try {
			initSocket();
			dos = new DataOutputStream(socket.getOutputStream());
			while (true) {
				ByteBuffer buff = MessageUtility.packMessage(chatMessage, senderId, receiverId, receiverTypeEnum);
				byte[] b = buff.array();
				dos.write(b, 0, b.length);
				System.out.println("written: " + new String(b, "UTF-8"));
				dos.flush();
				if (logger.isDebugEnabled()) {
					logger.debug("Sent: " + chatMessage);
				}
			}

		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				dos.close();
				socket.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

		}

	}

	/**
	 * 
	 * @param userName
	 * @param password
	 * @param senderId
	 */

	public void logInMessage(String userName, String password, String senderId) {
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			while (true) {
				ByteBuffer buff = MessageUtility.packLogInMessage(userName, password, senderId);
				byte[] b = buff.array();

				dos.write(b, 0, b.length);

				System.out.println("written: " + new String(b, "UTF-8"));
				dos.flush();
				System.out.println("Sent: " + userName);
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		} finally {
			try {
				dos.close();
				socket.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

	}

	public void signUp() {

	}

	public void initSocket() throws UnknownHostException, IOException {
		HOST = PropertyReader.getInstance().getPropertyValue("host");
		socket = new Socket(HOST, PORT);
	}

	public static void main(String[] args) throws UnknownHostException, IOException {

		getInstance().sendChatMessage("hello", "123456789", "123456745", ReceiverTypeEnum.INDIVIDUAL_MSG);

	}

}
