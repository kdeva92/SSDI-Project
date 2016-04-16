/**
 * 
 */
package org.ChatApplication.server.sender;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.ChatApplication.server.message.Message;

/**
 * @author Devdatta
 *
 */
public interface ISender {

	public void sendMessage(String client, ByteBuffer byteBuffer );
}
