package org.ChatApplication.data.service;

import org.ChatApplication.data.DAO.DAOObjectFactory;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.util.HibernateSessionUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

/**
 * 
 * @author Komal
 *
 */
public class UserService {

	private static UserService instance;
	final static Logger logger = Logger.getLogger(UserService.class);

	public static UserService getInstance() {
		if (instance == null) {
			synchronized (UserService.class) {
				if (instance == null) {
					instance = new UserService();
				}
			}
		}
		return instance;
	}

	public void createUser(User user) throws Exception {
		logger.info("Entering createUser");
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		try {
			DAOObjectFactory.getUserDAO().createUser(user);
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw new Exception(e.getMessage());
		}
	}
}
