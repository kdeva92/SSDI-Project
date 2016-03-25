package org.ChatServer.receiver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.ChatApplication.data.DAO.UserDAO;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 *         Start class of chat application. Main thread to accept new clients
 *         Internally creates NioServerModule to handle non-blocking IO of
 *         accepted clients
 */
public class ServerModule {
	private SocketChannel client;
	private ServerSocketChannel server;
	//HandlerThread handlerThread = new HandlerThread();
	NioServerModule nioServerModule;
	private boolean startFlag = false;
	private int port;
	private final static Logger logger = Logger.getLogger(ServerModule.class);

	public void start(int port) throws IllegalStateException, IOException {
		nioServerModule = NioServerModule.getNioServerModule();
		if (startFlag) {
			IllegalStateException e = new IllegalStateException("Server already running on port " + port);
			logger.error("Attempt to start already running server", e);
			throw e;
		}
		this.port = port;
		logger.trace("Attempting to start server on port: " + port);
		try {
			server = ServerSocketChannel.open();
			SocketAddress portAdd = new InetSocketAddress(port);
			server.socket().bind(portAdd);
			startFlag = true;
			logger.trace("Server bind complete, going to start message handler");
			new Thread(nioServerModule).start();
			logger.info("Server ready to accept");
			while (true) {
				client = server.accept();
				logger.debug("Accepted: " + client.getRemoteAddress());
				nioServerModule.addClient(client);
				logger.trace("Client " + client.getRemoteAddress() + " sent to handler");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}