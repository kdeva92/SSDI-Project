package org.ChatApplication.data.util;

import org.hibernate.SessionFactory;

/**
 * 
 * @author Komal
 *
 */
public class HibernateSessionUtil {

	public static SessionFactory getCurrentSessionTransaction() {

		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
		return sessionFactory;

	}

}
