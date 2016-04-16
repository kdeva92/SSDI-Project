package org.ChatApplication.data.DAO;

import java.util.ArrayList;
import java.util.List;

import org.ChatApplication.data.entity.Group;
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

	public User getUser(String userName, String password) throws HibernateException, Exception {
		logger.info("Entering getUser");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Query query = sessionFactory.getCurrentSession()
				.createQuery("from User where email =:email or ninerId =:ninerId and password =:password");
		query.setParameter("email", userName);
		query.setParameter("ninerId", userName);
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
				.createQuery("from User where email =:email or ninerId =:ninerId or firstName =:firstName");
		query.setParameter("email", searchString);
		query.setParameter("ninerId", searchString);
		query.setParameter("firstName", searchString);
		List<User> users = query.list();
		if (users == null) {
			users = new ArrayList<User>();
		}
		logger.info("Leaving getUser");
		return users;
	}

	public List<User> getUsers(List<String> ninerIds) throws HibernateException, Exception {
		logger.info("Entering getUser");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		Query query = sessionFactory.getCurrentSession().createQuery("from User where ninerId IN(:ninerId)");
		query.setParameterList("ninerId", ninerIds);
		List<User> users = query.list();
		if (users == null) {
			users = new ArrayList<User>();
		}
		logger.info("Leaving getUser");
		return users;
	}

	public void createGroup(Group group) {
		logger.info("Entering createGroup");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		sessionFactory.getCurrentSession().save(group);
		logger.info("Leaving createGroup");
	}

	public Group getGroup(int groupId) {

		logger.info("Entering getGroup");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Query query = sessionFactory.getCurrentSession().createQuery("from Group where grpouId =:grpouId");
		query.setParameter("grpouId", groupId);
		List<Group> groups = query.list();
		if (groups == null || groups.size() <= 0) {
			return null;
		}
		logger.info("Leaving getGroup");
		return groups.get(0);

	}

	public Group updateGroup(Group group) {
		logger.info("Entering createGroup");
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		sessionFactory.getCurrentSession().update(group);
		logger.info("Leaving createGroup");
		return group;
	}
}
