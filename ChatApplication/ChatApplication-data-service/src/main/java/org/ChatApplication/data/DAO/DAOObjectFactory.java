package org.ChatApplication.data.DAO;

/**
 * 
 * @author Komal
 *
 */
public abstract class DAOObjectFactory {

	public static UserDAO getUserDAO() {
		return UserDAO.getInstance();
	}
}
