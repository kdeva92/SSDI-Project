/**
 * 
 */
package org.ChatServer.receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.ChatServer.sender.ClientHolder;
import org.apache.log4j.Logger;


/**
 * @author Devdatta
 *
 *
 *	Class for java nio thread to control non blocking io of accepted clients
 */
public class NioServerModule {

	private final static Logger logger = Logger.getLogger(NioServerModule.class);
	private NioServerModule module;
	Selector s;
	ClientHolder clientHolder;

	NioServerModule getNioServerModule() throws IOException{
		if(module != null){
			return module;
		}
		if(logger.isTraceEnabled())
			logger.trace("creating new NioServerModule");
		module = new NioServerModule();
		module.init();
		return module;
	}

	public void init() throws IOException{
		s = Selector.open();		
		clientHolder = ClientHolder.getClientHolder();
		logger.trace("init complete for NioServerModule");
	}

	public void addClient(SocketChannel sc) throws IOException {
		sc.configureBlocking(false);
		//register socketchannel as attribute to reduce lookup time in map
		SelectionKey key = sc.register(s, SelectionKey.OP_READ, sc);
		//we still need map to keep track and keepalive 
		clientHolder.addClient(key, sc);
		if(logger.isDebugEnabled())
			logger.debug("selector set for channel: "+sc.getRemoteAddress());
	}

	public void run() {
		Set<SelectionKey> keyset;
		logger.debug("HandlerThread running..");
		while(true){
			int readyChannels = 0;
			try {
				readyChannels = s.select(100);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("selector failed to select", e1);
			}
			if(readyChannels == 0){
//				try {
//					Thread.sleep(300);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				continue;
			}

			keyset = s.selectedKeys();
			if(logger.isTraceEnabled())
				logger.trace("Selector selected keys: "+keyset.size());
			for (Iterator iterator = keyset.iterator(); iterator.hasNext();) {
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				try {
					Object clientObj = selectionKey.attachment();
					if(! (clientObj instanceof SocketChannel))
						System.err.println("ERROR client object not a socket channel");

					ObjectInputStream ois = new ObjectInputStream(((SocketChannel)clientObj).socket().getInputStream());
					String s = (String)ois.readObject();
					System.out.println("String is: '" + s + "'");

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			keyset.clear();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
