package org.ChatApplication.data.DAO;

import java.util.List;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.util.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
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

	public User getUser(String email, String password) throws HibernateException, Exception {
		logger.info("Entering getUser");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from User where email = :email and password =: password");
		query.setParameter("email", email);
		query.setParameter("password", password);
		List<User> users = query.list();
		if (users == null || users.size() <= 0) {
			return null;
		}
		logger.info("Leaving getUser");
		return users.get(0);

	}

	public List<User> getUsers(String searchString) throws HibernateException, Exception {
		logger.info("Entering getUser");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from User where email = :email or ninerId =: ninerId or firstName =:firstname");
		query.setParameter("email", searchString);
		query.setParameter("ninerId", searchString);
		query.setParameter("firstName", searchString);
		List<User> users = query.list();
		logger.info("Leaving getUser");
		return users;
	}

}
