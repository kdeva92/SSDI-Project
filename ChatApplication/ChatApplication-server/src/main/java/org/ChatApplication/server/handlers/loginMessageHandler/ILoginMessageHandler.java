/**
 * 
 */
package org.ChatApplication.server.handlers.loginMessageHandler;

import org.ChatApplication.data.entity.User;

/**
 * @author Devdatta
 *
 */
/**
 * 
 * This interface does not extend the IMessageHandler as the handling of login
 * messages is different than other messages, also this needs to be performed on
 * nio thread itself.
 *
 */
public interface ILoginMessageHandler {

	public User validateLogin(User user);
}
