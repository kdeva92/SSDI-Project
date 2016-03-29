package org.ChatApplication.ui.service.connector;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * 
 * @author Komal
 *
 */
public class RecieverController {
	Socket sock = null;
	private Socket socket;
	private DataInputStream dis;
	private static String HOST = "127.0.0.1";
	private static int PORT = 1515;
	private static RecieverController instance;

	private Logger logger = Logger.getLogger(getClass());

	public static RecieverController getInstance() {
		if (instance == null) {
			instance = new RecieverController();
		}
		return instance;
	}

	public void receiveChatMessage() {
		try {
			initSocket();
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.println("reader started ");

			while (true) {
				String incomingMessage = null;
				try {
					incomingMessage = dis.readUTF();
				} catch (IOException e) {
					logger.error(e.getMessage());
				} finally {
					dis.close();
					socket.close();
				}
			}
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	public void initSocket() throws UnknownHostException, IOException {
//		 HOST = PropertyReader.getInstance().getPropertyValue("host");
		socket = new Socket(HOST, PORT);
	}

	public static void main(String[] args) {

	}
}
