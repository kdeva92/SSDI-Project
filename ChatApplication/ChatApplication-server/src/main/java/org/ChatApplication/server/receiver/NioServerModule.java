
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
import org.ChatApplication.common.util.MessageUtility;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;
import org.ChatApplication.server.handlers.loginMessageHandler.ILoginMessageHandler;
import org.ChatApplication.server.handlers.loginMessageHandler.LoginHandlerFactory;
import org.ChatApplication.server.handlers.messageHandler.IMessageHandler;
import org.ChatApplication.server.handlers.messageHandler.MessageHandlerFactory;
import org.ChatApplication.server.handlers.signupMessageHandler.ISignupMessageHandler;
import org.ChatApplication.server.handlers.signupMessageHandler.SignupMessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.sender.ClientHolder;
import org.apache.log4j.Logger;

/**
 * @author Devdatta
 */
/**
 * 
 * Class for java nio thread to control non blocking io of accepted clients
 */
public class NioServerModule implements Runnable {

	private ILoginMessageHandler loginHandler = LoginHandlerFactory.getFactory().getLoginMessageandler();//
	private ISignupMessageHandler signupHandler = SignupMessageHandler.getMessageHandler();
	private static final IMessageHandler mssageHandler = MessageHandlerFactory.getFactory().getMessageandler();// MessageHandler.getMessageHandler();
	private final static Logger logger = Logger.getLogger(NioServerModule.class);
	private static NioServerModule module;
	Selector selector;
	ClientHolder clientHolder;

	private NioServerModule() {
		super();
	}

	public static NioServerModule getNioServerModule() throws IOException {
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
							UserVO userVO = ByteToEntityConverter.getInstance().getUser(message.getData());
							User user = new User();
							user.setNinerId(userVO.getNinerId());
							user.setPassword(userVO.getPassword());
							loginHandler.validateLogin(user, selectionKey);
							continue;
						}
						if (message.getType() == MessageTypeEnum.CHAT_MSG || message.getType()==MessageTypeEnum.FILE_MSG) {
							System.out.println("NIO chat message");
//							//--working
							buff.flip();
//							//message.setData(buff.array());
							
							//---test
//							System.out.println("no flip array: "+new String( buff.array()));
//							buff.flip();
//							System.out.println("setting data: "+new String( buff.array()));
//							message.setData(buff.array());
							//continue;
						}
						if(message.getType() == MessageTypeEnum.SIGNUP){
							signupHandler.doSignup(selectionKey, message);
							continue;
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
