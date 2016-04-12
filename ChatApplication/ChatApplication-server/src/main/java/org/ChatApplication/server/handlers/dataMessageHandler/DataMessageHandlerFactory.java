/**
 * 
 */
package org.ChatApplication.server.handlers.dataMessageHandler;

/**
 * @author deva
 *
 */
public class DataMessageHandlerFactory {

	private static DataMessageHandlerFactory dataMessageHandlerFactory;
	private DataMessageHandlerFactory(){
		
	}
	public static DataMessageHandlerFactory getFactory() {
		if(dataMessageHandlerFactory == null){
			dataMessageHandlerFactory = new DataMessageHandlerFactory();
		}
		return dataMessageHandlerFactory;
	}
	
	//if property set to Test return other handler
	public IDataMessageHandler getLoginMessageandler() {
		return DataMessageHandler.getDataMessageHandler();
	}

}
