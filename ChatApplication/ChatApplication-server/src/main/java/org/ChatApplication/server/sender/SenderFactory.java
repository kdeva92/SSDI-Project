/**
 * 
 */
package org.ChatApplication.server.sender;

/**
 * @author deva
 *
 */
public class SenderFactory {

	private static SenderFactory senderFactory;

	private SenderFactory() {

	}

	public static SenderFactory getFactory() {
		if (senderFactory == null) {
			senderFactory = new SenderFactory();
		}
		return senderFactory;
	}

	// if property set to Test return other handler
	public ISender getSender() {
		return ServerSender.getSender();
	}

}
