package org.ChatApplication.server.message;

/**
 * 
 * @author Komal
 *
 */
public enum ReceiverTypeEnum {

	INDIVIDUAL_MSG(0), GROUP_MSG(1);

	private int msgType;

	private ReceiverTypeEnum(int msgType) {
		this.setMsgType(msgType);
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	
	public int getIntEquivalant() {
		return msgType;
	}
}
