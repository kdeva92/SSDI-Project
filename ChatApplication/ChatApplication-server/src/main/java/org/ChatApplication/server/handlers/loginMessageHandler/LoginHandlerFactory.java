/**
 * 
 */
package org.ChatApplication.server.handlers.loginMessageHandler;

/**
 * @author Devdatta
 *
 */

/**
 * Factory class to create Login Handlers
 * 
 */
public class LoginHandlerFactory {

	private static LoginHandlerFactory loginHandlerFactory;

	private LoginHandlerFactory() {

	}

	public static LoginHandlerFactory getFactory() {
		if (loginHandlerFactory == null) {
			loginHandlerFactory = new LoginHandlerFactory();
		}
		return loginHandlerFactory;
	}

	// if property set to Test return other handler
	public ILoginMessageHandler getLoginMessageandler() {
		return LoginMessageHandler.getMessageHandler();
	}
}
