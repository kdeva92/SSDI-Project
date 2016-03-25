/**
 * 
 */
package org.ChatApplication.server.receiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.ChatApplication.server.sender.ClientHolder;
import org.apache.log4j.Logger;


/**
 * @author Devdatta
 *
 *
 *	Class for java nio thread to control non blocking io of accepted clients
 */
public class NioServerModule implements Runnable {

	private final static Logger logger = Logger.getLogger(NioServerModule.class);
	private static NioServerModule module;
	Selector s;
	ClientHolder clientHolder;
	private NioServerModule(){super();}
	
	static NioServerModule getNioServerModule() throws IOException{
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

					SocketChannel client = (SocketChannel)clientObj;
					System.out.println("Selected: "+client.getRemoteAddress());
					ByteBuffer buff = ByteBuffer.allocate(512);
					int size = client.read(buff);
					buff.flip();
					System.out.println("read size: "+size);
					System.out.println("data buff: "+new String(buff.array()));
					byte[] arr = new byte[size];
					System.out.println("buffer data: ");
					for (int i = 0; i < arr.length; i++) {
						System.out.print(" "+ (char)buff.get(i));
					}
					buff.get(arr,0,arr.length);
					System.out.println("array data: ");
					for (int i = 0; i < arr.length; i++) {
						System.out.print(" "+ arr[i]);
					}
					System.out.println("String size" +arr.length+ " data: "+ new String(arr));
					String str = new String(arr,"UTF-8");


				} catch (IOException e) {
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
