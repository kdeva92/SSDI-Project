/**
 * 
 */
package org.ChatApplication.common.util;

import java.nio.ByteBuffer;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;

/**
 * @author Devdatta
 *
 */
public class MessagePackTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ByteBuffer message = MessageUtility.packMessage("Demo Message123".getBytes(), "000000000", "000000000", ReceiverTypeEnum.INDIVIDUAL_MSG, MessageTypeEnum.CHAT_MSG);
//		for(int i=0;i<50;i++)
//			System.out.print(" "+message.get());
//		
		
		System.out.println("Message: "+new String(message.array()));
		message.flip();
		Message msg = MessageUtility.getMessage(message);
		System.out.println("Message: "+new String(msg.getData()).trim());
		System.out.println("Receiver "+msg.getReceiver());
		System.out.println("Sender "+msg.getSender());
		System.out.println("Receiver Type "+msg.getReceiverType());
		System.out.println("Message type "+msg.getType());
		
	}

}
