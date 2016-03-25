/**
 * 
 */
package org.ChatServer.receiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.ChatApplication.data.DAO.UserDAO;
import org.apache.log4j.Logger;

import project.HandlerThread;

/**
 * @author Devdatta
 *
 */
public class ServerModule {
	private SocketChannel client;
	private ServerSocketChannel server;
	HandlerThread handlerThread = new HandlerThread();
	private boolean startFlag = false;
	private int port;
	private final static Logger logger = Logger.getLogger(ServerModule.class);
	
	public void start(int port) throws IllegalStateException {
			if(startFlag){
				IllegalStateException e = new IllegalStateException("Server already running on port "+port);
				logger.error("Attempt to start already running server", e);
				throw e;
			}
			this.port=port;	
			logger.trace("Attempting to start server on port: "+port);
		try {
			server = ServerSocketChannel.open();
			SocketAddress portAdd = new InetSocketAddress(port);
			server.socket().bind(portAdd);
			logger.trace("Server bind complete, going to start message handler");
			handlerThread.init();
			new Thread(handlerThread).start();
			logger.trace("");
			System.out.println("Server ready to accept");
			while(true){
				client = server.accept();
				System.out.println("Accepted: "+client.getRemoteAddress());
				handlerThread.addClient(client);
				System.out.println("Client "+client.getRemoteAddress()+" sent to handler");
			}
			startFlag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

