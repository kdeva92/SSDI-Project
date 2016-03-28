/**
 * 
 */
package org.ChatApplication.server.handlers.messageHandler;

import org.ChatApplication.server.message.Message;

/**
 * @author Devdatta
 *
 */
public interface IMessageHandler {

	public void handleMessage(Message message);

}
