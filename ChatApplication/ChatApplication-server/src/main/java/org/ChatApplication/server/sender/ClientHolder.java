package org.ChatApplication.server.sender;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * @author Devdatta
 *
 *
 *         Class to hold live clients and their socket channels
 */

public class ClientHolder {

	// map of channel to selectionkey - to be used by keep alive check
	private ConcurrentHashMap<String, ClientData> clientMap;
	static private ClientHolder holder = null;
	private final static Logger logger = Logger.getLogger(ClientHolder.class);

	public static ClientHolder getClientHolder() {
		if (holder != null)
			return holder;
		holder = new ClientHolder();
		if (logger.isTraceEnabled())
			logger.trace("created new ClientHolder");
		return holder;
	}

	private ClientHolder() {
		// TODO Auto-generated constructor stub
		clientMap = new ConcurrentHashMap<String, ClientData>();
	}

	public void addClient(String clientId, SelectionKey key, SocketChannel channel) {
		clientMap.put(clientId, new ClientData(channel, key));
		if (logger.isTraceEnabled()) {
			try {
				logger.trace("added client " + channel.getRemoteAddress() + " to holder");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Set<String> getAllConnectedClients() {
		if (logger.isTraceEnabled())
			logger.trace("Holder return " + clientMap.keySet().size() + " clients");
		return clientMap.keySet();
	}

	public ClientData getClientData(String clientId) {
		if (logger.isTraceEnabled()) {
			logger.trace("returning selection key for " + clientId);
		}
		return clientMap.get(clientId);
	}

	/**
	 * @param socketChannel
	 *            Removes passed socket channel from client holder Expensive and
	 *            to be used only if id of client id is not known
	 */
	public void removeClient(SocketChannel socketChannel) {
		// TODO Auto-generated method stub
		Set<String> allClients = clientMap.keySet();
		for (Iterator iterator = allClients.iterator(); iterator.hasNext();) {
			String clientId = (String) iterator.next();
			if (clientMap.get(clientId).getSocketChannel().equals(socketChannel)) {
				clientMap.remove(clientId);
				break;
			}
		}

	}
}
