/**
 * 
 */
package org.ChatApplication.server.handlers.dataMessageHandler;

import org.ChatApplication.server.message.Message;

/**
 * @author Devdatta
 *
 */
public class DataMessageHandler implements IDataMessageHandler {

	private static DataMessageHandler dataMessageHandler;
	
	private DataMessageHandler(){}
	
	public static DataMessageHandler getDataMessageHandler() {
		if(dataMessageHandler != null )
			return dataMessageHandler;
		dataMessageHandler = new DataMessageHandler();
		return dataMessageHandler;
		
	}

	public void handleMessage(Message message) {
		// TODO Auto-generated method stub
		System.out.println("Message: "+new String(message.getData().array()));
	}
}
