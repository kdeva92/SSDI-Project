/**
 * 
 */
package org.ChatApplication.server.handlers.loginMessageHandler;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.ChatApplication.data.DAO.UserDAO;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.service.UserService;
import org.ChatApplication.server.handlers.dataMessageHandler.DataMessageHandler;
import org.ChatApplication.server.handlers.dataMessageHandler.IDataMessageHandler;
import org.ChatApplication.server.message.Message;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 * @author Devdatta
 *
 */
public class LoginMessageHandler implements ILoginMessageHandler {

	//ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<Message>();
	private final static Logger logger = Logger.getLogger(LoginMessageHandler.class);
	//private HandlerThread handlerThread;
	//private Thread thread;

	private LoginMessageHandler() {
//		handlerThread = new HandlerThread();
//		thread = new Thread(handlerThread);
//		thread.start();
	}

	private static LoginMessageHandler messageHandler;

	public static LoginMessageHandler getMessageHandler() {
		if (messageHandler != null)
			return messageHandler;
		messageHandler = new LoginMessageHandler();
		return messageHandler;
	}

	public User validateLogin(User user) {
		// TODO Auto-generated method stub
		try {
			return UserService.getInstance().getUser(user.getNinerId(), user.getPassword());
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error in getting user from db", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Error in getting user from db", e);
		}
		return null;
	}

	
//	public void handleMessage(Message message) {
//		// TODO Auto-generated method stub
//		messageQueue.add(message);
//	}
//
//	private class HandlerThread implements Runnable {
//
//		private IDataMessageHandler dataMessageHandler = DataMessageHandler.getDataMessageHandler();
//
//		public void run() {
//			// TODO Auto-generated method stub
//			while (true) {
//				Message message = messageQueue.poll();
//				if (message == null) {
//					try {
//						Thread.sleep(50);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					continue;
//				}
//				//Handle login request
//				UserDAO dao = UserDAO.getInstance();
//				//dao.getUser(email, password)
//			}
//		}
//
//	}

}
