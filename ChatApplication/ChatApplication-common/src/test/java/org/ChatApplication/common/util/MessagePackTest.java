/**
 * 
 */
package org.ChatApplication.common.util;

import java.nio.ByteBuffer;
import java.util.List;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;

import junit.framework.TestCase;

/**
 * @author Devdatta
 *
 */
public class MessagePackTest extends TestCase {

	public void testChatMessagePack() {
		String messageData = "Demo Message123";
		String sender = "000000000";
		String receiver = "999999999";
		ReceiverTypeEnum receiverTypeEnum = ReceiverTypeEnum.INDIVIDUAL_MSG;
		MessageTypeEnum messageTypeEnum = MessageTypeEnum.CHAT_MSG;

		List<ByteBuffer> message = MessageUtility.packMessageToArray(messageData, sender, receiver, receiverTypeEnum,
				messageTypeEnum);
		//
		message.get(0).flip();
		Message msg = MessageUtility.getMessage(message.get(0));

		if (msg.getType() == messageTypeEnum && msg.getSender().equals(sender) && msg.getReceiver().equals(receiver)
				&& msg.getReceiverType() == receiverTypeEnum.getIntEquivalant())
			assertTrue(true);
		else
			assertTrue(false);
	}

	public void testInstructionMessagePack() {
		String messageData = "Demo Message123";
		String sender = "000000000";
		String receiver = "999999999";
		ReceiverTypeEnum receiverTypeEnum = ReceiverTypeEnum.GROUP_MSG;
		MessageTypeEnum messageTypeEnum = MessageTypeEnum.CREATE_GROUP;

		 List<ByteBuffer> message = MessageUtility.packMessageToArray(messageData, sender, receiver, receiverTypeEnum,
				messageTypeEnum);

		message.get(0).flip();
		Message msg = MessageUtility.getMessage(message.get(0));

		if (msg.getType() == messageTypeEnum && msg.getSender().equals(sender) && msg.getReceiver().equals(receiver)
				&& msg.getReceiverType() == receiverTypeEnum.getIntEquivalant())
			assertTrue(true);
		else
			assertTrue(false);
	}
	

//	public void arrayChatMessagePack() {
//		String messageData = "Demo Message123";
//		String sender = "000000000";
//		String receiver = "999999999";
//		ReceiverTypeEnum receiverTypeEnum = ReceiverTypeEnum.INDIVIDUAL_MSG;
//		MessageTypeEnum messageTypeEnum = MessageTypeEnum.CHAT_MSG;
//
//		List<ByteBuffer> message = MessageUtility.packMessageToArray(messageData, sender, receiver, receiverTypeEnum,
//				messageTypeEnum);
//
//		message.get(0).flip();
//		Message msg = MessageUtility.getMessage(message.get(0));
//
//		if (msg.getType() == messageTypeEnum && msg.getSender().equals(sender) && msg.getReceiver().equals(receiver)
//				&& msg.getReceiverType() == receiverTypeEnum.getIntEquivalant())
//			assertTrue(true);
//		else
//			assertTrue(false);
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new MessagePackTest().testChatMessagePack();
	}

}
