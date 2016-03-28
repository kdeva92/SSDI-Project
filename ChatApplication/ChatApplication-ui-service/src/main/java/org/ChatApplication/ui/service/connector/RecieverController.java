package org.ChatApplication.ui.service.connector;

import java.net.Socket;

/**
 * 
 * @author Komal
 *
 */
public class RecieverController implements Runnable {
	Socket sock = null;

	public RecieverController(Socket s) {
		this.sock = s;
	}

	public void run() {
		// TODO Auto-generated method stub
		if (sock.isConnected()) {
			// TODO
		}
	}

}
