/**
 * 
 */
package org.ChatApplication.server.util;

import java.nio.ByteBuffer;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;

/**
 * @author Devdatta
 *
 */
public final class MessageUtility {

	//constants
	static int SENDER_SIZE =9;
	static int RECEIVER_SIZE =9;

	//private constructor 
	private MessageUtility(){}

	public static Message getMessage(ByteBuffer buffer) {

		Message message = new Message();
		System.out.println("Reading buffer: "+new String(buffer.array()));
		//message type
		switch (buffer.get()) {
		case 1:
			message.setType(MessageTypeEnum.CHAT_MSG);
			System.out.println("type = chat message");
			break;
		default:
			System.out.println("default type");
			message.setType(null);
			break;
		}
		//empty byte
		buffer.get();
		//sender
		byte[] sender = new byte[9];
		buffer.get(sender, 0, 9);
		message.setSender(new String(sender));
		//set receiver
		buffer.get(sender, 0, 9);
		message.setReceiver(new String(sender));
		System.out.println("receiver type: "+buffer.get());
		//no of packets
		message.setNoOfPackets(buffer.getInt());
		//# of packet
		message.setPacketNo(buffer.getInt());
		//length
		int msgSize = buffer.getShort();
		message.setLength(msgSize);
		message.setData(buffer);
		//		byte[] data = new byte[msgSize];
		//		buffer.get(data, 0, msgSize);
		//		message.se

		return new Message();
	}


}
