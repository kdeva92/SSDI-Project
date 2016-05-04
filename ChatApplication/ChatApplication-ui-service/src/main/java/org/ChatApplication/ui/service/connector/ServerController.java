package org.ChatApplication.ui.service.connector;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.ChatApplication.ui.service.observer.MessageListener;

/**
 * 
 * @author Komal
 *
 */
public class ServerController {
	private Socket socket;
	private SenderController senderController;
	private NonBufferedReceiver nonBufferedReceiver;
	private static String HOST = "localhost";
	private static int PORT = 1515;

	public ServerController(MessageListener ml) throws UnknownHostException, IOException {
		socket = new Socket(HOST, PORT);
		senderController = new SenderController(socket);
		System.out.println("Sender Started");
		nonBufferedReceiver = new NonBufferedReceiver(socket,ml);
		System.out.println("Reciever Started");
		// receiverController = new RecieverController(socket);
		// receiverController.run();
	}

	public SenderController getSenderController() {
		return senderController;
	}

	public void setSenderController(SenderController senderController) {
		this.senderController = senderController;
	}
	
	public void terminateConnection() throws IOException{
		socket.close();
	}

}
