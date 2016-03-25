package org.ChatServer.sender;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.ChatServer.receiver.ServerModule;
import org.apache.log4j.Logger;


/**
 * @author Devdatta
 *
 *
 *	Class to hold live clients and their socket channels
 */

public class ClientHolder {

	//map of channel to selectionkey - to be used by keep alive check
	ConcurrentHashMap<SocketChannel,SelectionKey> keyChannelMap;
	static private ClientHolder holder = null;
	private final static Logger logger = Logger.getLogger(ClientHolder.class);

	public static ClientHolder getClientHolder() {
		if(holder != null)
			return holder;
		holder = new ClientHolder();
		if(logger.isTraceEnabled())
			logger.trace("created new ClientHolder");
		return holder;
	}

	private ClientHolder() {
		// TODO Auto-generated constructor stub
		keyChannelMap = new ConcurrentHashMap<SocketChannel,SelectionKey>();
	}

	public void addClient(SelectionKey key, SocketChannel channel) {
		keyChannelMap.put(channel,key);		
		if(logger.isTraceEnabled()){
			try {
				logger.trace("added client "+channel.getRemoteAddress()+" to holder");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Set<SocketChannel> getAllConnectedClients() {
		if(logger.isTraceEnabled())
			logger.trace("Holder return "+ keyChannelMap.keySet().size()+" clients");
		return keyChannelMap.keySet();	
	}

	public SelectionKey getSelectionKeyForClient(SocketChannel client) {
		if(logger.isTraceEnabled()){
			try {
				logger.trace("returning selection key for "+client.getRemoteAddress()+" as "+keyChannelMap.get(client));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keyChannelMap.get(client);	
	}
}
