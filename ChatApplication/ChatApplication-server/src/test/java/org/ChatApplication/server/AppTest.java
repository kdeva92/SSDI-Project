package org.ChatApplication.server;

import org.ChatApplication.data.DAO.UserDAO;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
		createUserTest();
	}
	


	public void createUserTest() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		User user = new User();
		user.setEmail("kingale@uncc.edu");
		user.setFirstName("Komal");
		user.setLastName("Ingale");
		user.setNinerId("800908989");
		user.setPassword("demo123");

		try {
			User retrievedUser = UserDAO.getInstance().getUser("kingale@uncc.edu", "demo123");
			if (retrievedUser.getEmail().equals(user.getEmail())) {
				assertTrue(false);
			}
		} catch (HibernateException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
		try {
			UserDAO.getInstance().createUser(user);
		} catch (HibernateException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
		assertTrue(true);
	}
	
	
}
