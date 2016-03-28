
/**
 * 
 */
package org.ChatApplication.server.message;

import java.nio.ByteBuffer;

/**
 * @author Devdatta
 *  
 */
/**
 * 
 * Representation of message. Holds message bytes and message header
 *
 */
public class Message {

	public static final int MAX_MESSAGE_SIZE = 1400;

	private MessageTypeEnum type;
	private String sender;
	private String receiver;
	private boolean groupReceiver;
	private int noOfPackets;
	private int packetNo;
	private int length;
	private ByteBuffer data;
	private int receiverType;

	public int getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(int receiverType) {
		this.receiverType = receiverType;
	}

	public MessageTypeEnum getType() {
		return type;
	}

	public void setType(MessageTypeEnum type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public boolean isGroupReceiver() {
		return groupReceiver;
	}

	public void setGroupReceiver(boolean groupReceiver) {
		this.groupReceiver = groupReceiver;
	}

	public int getNoOfPackets() {
		return noOfPackets;
	}

	public void setNoOfPackets(int noOfPackets) {
		this.noOfPackets = noOfPackets;
	}

	public int getPacketNo() {
		return packetNo;
	}

	public void setPacketNo(int packetNo) {
		this.packetNo = packetNo;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public ByteBuffer getData() {
		return data;
	}

	public void setData(ByteBuffer data) {
		this.data = data;
	}

}
