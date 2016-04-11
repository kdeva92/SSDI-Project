/**
 * 
 */
package org.ChatApplication.server.receiver;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.server.handlers.loginMessageHandler.ILoginMessageHandler;
import org.ChatApplication.server.handlers.loginMessageHandler.LoginMessageHandler;
import org.ChatApplication.server.handlers.messageHandler.MessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;
import org.ChatApplication.server.sender.ClientHolder;
import org.ChatApplication.server.sender.ServerSender;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 */
/**
 * 
 * Class for java nio thread to control non blocking io of accepted clients
 */
public class NioServerModule implements Runnable {

	private ILoginMessageHandler loginHandler = LoginMessageHandler.getMessageHandler();
	private static final MessageHandler mssageHandler = MessageHandler.getMessageHandler();
	private final static Logger logger = Logger.getLogger(NioServerModule.class);
	private static NioServerModule module;
	Selector selector;
	ClientHolder clientHolder;

	private NioServerModule() {
		super();
	}

	static NioServerModule getNioServerModule() throws IOException {
		if (module != null) {
			return module;
		}
		if (logger.isTraceEnabled())
			logger.trace("creating new NioServerModule");
		module = new NioServerModule();
		module.init();
		return module;
	}

	public void init() throws IOException {
		selector = Selector.open();
		clientHolder = ClientHolder.getClientHolder();
		logger.trace("init complete for NioServerModule");
	}

	public void addClient(SocketChannel sc) throws IOException {
		sc.configureBlocking(false);
		// register socketchannel as attribute to reduce lookup time in map
		SelectionKey key = sc.register(selector, SelectionKey.OP_READ, sc);
		// we still need map to keep track and keepalive this will be done after
		// login success
		if (logger.isDebugEnabled())
			logger.debug("selector set for channel: " + sc.getRemoteAddress());
	}

	private void addClientToClientHolder(String clientId, SelectionKey key, SocketChannel sc) {
		clientHolder.addClient(clientId, key, sc);
		if (logger.isDebugEnabled()) {
			try {
				logger.debug("selector set for channel: " + sc.getRemoteAddress());
			} catch (IOException e) {
			}
		}
	}

	public void removeClient(SocketChannel sc) {
		clientHolder.removeClient(sc);
	}

	public void run() {
		Set<SelectionKey> keyset;
		logger.debug("NIO server thread running..");
		while (true) {
			int readyChannels = 0;
			try {
				readyChannels = selector.select(100);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				logger.error("selector failed to select", e1);
			}
			if (readyChannels == 0) {
				// try {
				// Thread.sleep(300);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				continue;
			}

			keyset = selector.selectedKeys();
			System.out.println("total Selected channels: " + keyset);
			if (logger.isTraceEnabled())
				logger.trace("Selector selected keys: " + keyset.size());
			for (Iterator iterator = keyset.iterator(); iterator.hasNext();) {
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				try {
					Object clientObj = selectionKey.attachment();
					if (!(clientObj instanceof SocketChannel))
						System.err.println("ERROR client object not a socket channel");

					SocketChannel client = (SocketChannel) clientObj;
					System.out.println("Selected: " + client.getRemoteAddress());
					ByteBuffer buff = ByteBuffer.allocate(Message.MAX_MESSAGE_SIZE);
					try {
						int size = client.read(buff);
						// end of stream
						if (size == -1) {
							removeClient(client);
							selectionKey.cancel();
						}
						// System.out.println("read size: " + size);
					} catch (IOException e) {
						System.out.println("Client disconnect.. removing " + client.getRemoteAddress());
						removeClient(client);

						selectionKey.cancel();
						continue;
					}
					buff.flip();
					// System.out.println("read size: "+size);
					// System.out.println("data buff: "+new
					// String(buff.array()));
					// byte[] arr = new byte[size];
					// System.out.println("buffer data: ");
					// for (int i = 0; i < arr.length; i++) {
					// System.out.print(" "+ (char)buff.get(i));
					// }
					// buff.get(arr,0,arr.length);
					// System.out.println("array data: ");
					// for (int i = 0; i < arr.length; i++) {
					// System.out.print(" "+ arr[i]);
					// }
					// System.out.println("String size" +arr.length+ " data: "+
					// new String(arr).trim());
					// String str = new String(arr,"UTF-8");
					Message message = null;
					try {
						message = MessageUtility.getMessage(buff);
						// Handle complete login messages here as special case
						if (message.getType() == MessageTypeEnum.LOG_IN_MSG) {
							System.out.println("Login message in nio module");
							User user = loginHandler
									.validateLogin(ByteToEntityConverter.getInstance().getUser(message.getData()));
							// force terminate to invalid client
							if (user == null) {
								selectionKey.cancel();
								client.close();
								continue;
							}
							// for valid login request
							addClientToClientHolder(user.getNinerId(), selectionKey, client);
							message.setData(
									MessageUtility
											.packMessage(EntityToByteConverter.getInstance().getBytes(user),
													message.getSender(), message.getReceiver(),
													ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.LOG_IN_MSG)
											.array());
							ServerSender.getSender().sendMessage(client, message);
							System.out.println("Login successful user added to client holder");
							continue;
						}
						if (message.getType() == MessageTypeEnum.CHAT_MSG) {
							System.out.println("NIO chat message");
							buff.flip();
							message.setData(buff.array());
						}
					} catch (BufferUnderflowException e) {
						iterator.remove();
						continue;
					}

					mssageHandler.handleMessage(message);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			keyset.clear();
			// try {
			// Thread.sleep(1000);
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

	}

}
