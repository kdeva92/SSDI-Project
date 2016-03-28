package org.ChatApplication.data.service;

import java.util.List;

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

	public User getUser(String email, String password) throws Exception {
		logger.info("Entering createUser");
		User user = null;
		try {
			user = DAOObjectFactory.getUserDAO().getUser(email, password);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return user;
	}

	public List<User> getUsers(String searchString) throws Exception {
		logger.info("Entering createUser");
		List<User> users = null;
		try {
			users = DAOObjectFactory.getUserDAO().getUsers(searchString);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return users;
	}

}
