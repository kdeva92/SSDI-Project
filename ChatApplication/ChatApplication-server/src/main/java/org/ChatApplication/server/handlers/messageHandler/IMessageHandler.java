/**
 * 
 */
package org.ChatApplication.server.handlers.messageHandler;

import java.nio.ByteBuffer;

/**
 * @author Devdatta
 *
 */
public interface IMessageHandler {

	public int handleMessage(ByteBuffer message);
	
}
