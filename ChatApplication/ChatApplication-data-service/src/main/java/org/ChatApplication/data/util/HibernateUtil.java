package org.ChatApplication.data.util;

import org.ChatApplication.data.entity.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

/**
 * 
 * @author Komal
 *
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			Configuration config = new Configuration();
			config.addAnnotatedClass(User.class);
			config.configure("resources/hibernate.cfg.xml");
			new SchemaUpdate(config).execute(true, true);

			return config.buildSessionFactory(
					new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry());
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void main(String[] args) {
		getSessionFactory();
	}
}