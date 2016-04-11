/**
 * 
 */
package org.ChatApplication.common.util;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import org.ChatApplication.server.message.Message;
import org.ChatApplication.server.message.MessageTypeEnum;
import org.ChatApplication.server.message.ReceiverTypeEnum;

/**
 * @author Devdatta
 *
 */
public final class MessageUtility {

	// constants
	static int SENDER_SIZE = 9;
	static int RECEIVER_SIZE = 9;

	// these constants are moved to Message class and referred from there
	// static String c = "u0095";
	// static String end = "u0004";
	// static byte START_OF_MESSAGE = c.getBytes()[0];
	// static byte END_OF_MESSAGE = end.getBytes()[0];

	// NEED TO CHANGE
	// static byte CHAT_MESSAGE = (byte) 1;
	// static byte USER_RECEIVER = (byte) 1;

	// private constructor
	private MessageUtility() {
	}

	public static Message getMessage(ByteBuffer buffer) throws BufferUnderflowException {

		Message message = new Message();
		System.out.println("Reading buffer: " + new String(buffer.array()));

		// pop start of message
		System.out.println("Empty pop:" + buffer.get());

		// message type
		int i = buffer.get();
		System.out.println("Messageutil type: " + i);
		message.setType(MessageTypeEnum.getMessageTypeEnumByIntValue(i));
		// switch (i) {
		// case 1:
		// message.setType(MessageTypeEnum.CHAT_MSG);
		// System.out.println("type = chat message");
		// break;
		// case 2:
		// message.setType(MessageTypeEnum.LOG_IN_MSG);
		// System.out.println("type = chat message");
		// break;
		// default:
		// // System.out.println("default type");
		// message.setType(null);
		// break;
		// }

		// empty byte
		buffer.get();

		// sender
		byte[] sender = new byte[9];
		buffer.get(sender, 0, 9);
		message.setSender(new String(sender));
		// set receiver
		buffer.get(sender, 0, 9);
		message.setReceiver(new String(sender));
		message.setReceiverType(buffer.get());
		// System.out.println("receiver type: "+buffer.get());
		// no of packets
		message.setNoOfPackets(buffer.getInt());
		// # of packet
		message.setPacketNo(buffer.getInt());
		// length
		int msgSize = buffer.getShort();
		message.setLength(msgSize);
		byte[] data = new byte[msgSize];
		buffer.get(data, 0, msgSize);
		message.setData(data);
		
		System.out.println("getMessage "+ data.length);
		for(int i1=0;i1<30 && i1 < data.length;i1++)
			System.out.println(i1+" "+data[i1]);
		return message;
	}

	public static ByteBuffer packMessage(byte[] message, String senderID, String receiverID,
			ReceiverTypeEnum receiverTypeEnum, MessageTypeEnum messageType) {
		// add loop here to return multiple buffers of packets

		ByteBuffer buffer = ByteBuffer.allocate(Message.MAX_MESSAGE_SIZE);

		// put start of message
		buffer = buffer.put(Message.START_OF_MESSAGE);
		// System.out.println("start of msg");
		// printBuff(buffer);

		// put message type as chat message
		buffer = buffer.put(messageType.getByteEquivalant());
		System.out.println("MessageUtil packmsg type put: "+ messageType.getByteEquivalant());

		// keep index 2 as empty
		buffer.put((byte) 0);

		// put sender id from index 3
		// add exception here if sender length not 9
		byte[] sender = senderID.getBytes();
		buffer = buffer.put(sender, 0, sender.length);

		// put receiver id from index 3 + 9*2 = 21
		// add exception here if receiver length not 9
		byte[] receiver = receiverID.getBytes();
		buffer = buffer.put(receiver, 0, receiver.length);

		// put receiver type at 21 + 9*2 = 39
		buffer.put((byte) receiverTypeEnum.getMsgType());

		// put # of packets
		buffer.putInt(1);

		// put packet #
		buffer.putInt(1);

		// put length of message
		buffer.putShort((short) (message.length));

		// put message
		buffer.put(message, 0, message.length);
		buffer.put(Message.END_OF_MESSAGE);
		System.out.println("Buffer put end of message");
		return buffer;
	}
	/*
	 * public static ByteBuffer packLogInMessage(String userName, String
	 * password, String senderId) {
	 * 
	 * // add loop here to return multiple buffers of packets
	 * 
	 * ByteBuffer buffer = ByteBuffer.allocate(100);
	 * 
	 * // put start of message buffer = buffer.put(Message.START_OF_MESSAGE); //
	 * System.out.println("start of msg"); // printBuff(buffer);
	 * 
	 * // put message type as chat message buffer = buffer.put(CHAT_MESSAGE);
	 * 
	 * // keep index 2 as empty buffer.put((byte) 0);
	 * 
	 * // put sender id from index 3 // add exception here if sender length not
	 * 9 byte[] sender = senderId.getBytes(); buffer = buffer.put(sender, 0,
	 * sender.length);
	 * 
	 * // put # of packets buffer.putInt(1);
	 * 
	 * // put packet # buffer.putInt(1); String message = "userName:" + userName
	 * + ",password:" + password;
	 * 
	 * // put length of message buffer.putShort((short) (message.length()));
	 * 
	 * // put message buffer.put(message.getBytes(), 0,
	 * message.getBytes().length); return buffer;
	 * 
	 * }
	 */

}
