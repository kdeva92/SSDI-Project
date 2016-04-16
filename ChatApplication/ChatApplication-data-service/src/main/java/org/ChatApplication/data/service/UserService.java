package org.ChatApplication.data.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ChatApplication.data.DAO.DAOObjectFactory;
import org.ChatApplication.data.entity.Group;
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
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		try {
			user = DAOObjectFactory.getUserDAO().getUser(email, password);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return user;
	}

	public List<User> getUsers(String searchString) throws Exception {
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
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

	public List<User> getUsers(List<String> ninerIds) throws Exception {
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		logger.info("Entering createUser");
		List<User> users = null;
		try {
			users = DAOObjectFactory.getUserDAO().getUsers(ninerIds);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return users;
	}

	public void createGroup(Group group) throws Exception {
		logger.info("Entering createGroup");
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		try {
			DAOObjectFactory.getUserDAO().createGroup(group);
			sessionFactory.getCurrentSession().getTransaction().commit();
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			sessionFactory.getCurrentSession().getTransaction().rollback();
			throw new Exception(e.getMessage());
		}

	}

	public Group getGroup(int groupId) throws Exception {

		logger.info("Entering createUser");
		Group group = null;
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		try {
			group = DAOObjectFactory.getUserDAO().getGroup(groupId);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return group;

	}

	public Group updateGroup(Group group) throws Exception {

		logger.info("Entering updateGroup");
		SessionFactory sessionFactory = HibernateSessionUtil.getCurrentSessionTransaction();
		try {
			group = DAOObjectFactory.getUserDAO().updateGroup(group);
		} catch (HibernateException e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}
		return group;

	}

	public static void main(String[] args) throws Exception {
//		List<String> ninerids = new ArrayList<String>();
//		ninerids.add("000000000");
//		ninerids.add("000000001");
//		List<User> users = UserService.getInstance().getUsers(ninerids);
//		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
//			User user = (User) iterator.next();
//			System.out.println(user.getEmail());
//		}
		Group group = new Group();
		group.setName("group1");
		getInstance().createGroup(group);
	}
}