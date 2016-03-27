/**
 * 
 */
package org.ChatApplication.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.ChatApplication.server.handlers.messageHandler.IMessageHandler;
import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;

/**
 * @author deva
 *
 */
public class ReflectionTest {

	public static void main(String[] args) {
		Message message = new Message();
		message.setType(MessageTypeEnum.CHAT_MSG);
		try {
			Class handlerClass = message.getType().getHandlerClass();
			Method instanceMethod = handlerClass.getMethod("getInstance", null);
			IMessageHandler handler = (IMessageHandler)instanceMethod.invoke(null, null);
			System.out.println("handler object "+handler);
			Method handleMethod = handlerClass.getMethod("handleMessage", null);
			handleMethod.invoke(handler, message);
			System.out.println("invoked handlerMessage ");
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
