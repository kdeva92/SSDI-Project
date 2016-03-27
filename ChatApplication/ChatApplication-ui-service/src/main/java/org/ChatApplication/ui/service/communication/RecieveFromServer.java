package org.ChatApplication.ui.service.communication;

import java.net.Socket;

public class RecieveFromServer implements Runnable {
	Socket sock = null;
	
	public RecieveFromServer(Socket s)
	{
		this.sock = s;
	}
	
	public void run() {
		// TODO Auto-generated method stub
			if(sock.isConnected())
				{
					// TODO
				}
	}

}
