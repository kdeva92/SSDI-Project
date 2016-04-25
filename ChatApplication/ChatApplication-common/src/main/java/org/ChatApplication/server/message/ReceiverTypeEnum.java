package org.ChatApplication.server.message;

import java.util.HashMap;

/**
 * 
 * @author Komal
 *
 */
public enum ReceiverTypeEnum {

	INDIVIDUAL_MSG(0), GROUP_MSG(1);

	private int msgType;

	private static HashMap<Integer, ReceiverTypeEnum> valueMap = new HashMap<Integer, ReceiverTypeEnum>();

	static {
		for (ReceiverTypeEnum e : ReceiverTypeEnum.values()) {
			valueMap.put(e.getIntEquivalant(), e);
		}
	}
	
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
	
	public static ReceiverTypeEnum getReceiverTypeEnumByIntValue(int value) {
		return valueMap.get(value);
	}
}
