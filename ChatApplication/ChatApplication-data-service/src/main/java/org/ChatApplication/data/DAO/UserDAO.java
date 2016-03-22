package org.ChatApplication.data.DAO;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * 
 * @author Komal
 *
 */
public class UserDAO {
	private static UserDAO instance;
	final static Logger logger = Logger.getLogger(UserDAO.class);

	public static UserDAO getInstance() {
		if (instance == null) {
			synchronized (UserDAO.class) {
				if (instance == null) {
					instance = new UserDAO();
				}
			}
		}
		return instance;
	}

	public void createUser(User user) throws HibernateException, Exception {
		logger.info("Entering createUser");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		sessionFactory.getCurrentSession().save(user);
		logger.info("Leaving createUser");
	}

}
