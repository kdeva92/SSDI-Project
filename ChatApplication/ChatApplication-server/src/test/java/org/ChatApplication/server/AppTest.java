package org.ChatApplication.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.ChatApplication.data.DAO.UserDAO;
import org.ChatApplication.data.entity.Group;
import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.service.UserService;
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
	 * @throws Exception 
	 */
	public void testApp() throws Exception {
		assertTrue(true);
		createGroup();
		createUserTest();
	}

	public void createUserTest() {
		User user = new User();
		user.setEmail("kingale1@uncc.edu");
		user.setFirstName("Komal");
		user.setLastName("Ingale");
		user.setNinerId("800908989");
		user.setPassword("demo123");

		try {
			User retrievedUser = UserService.getInstance().getUser("kingale1@uncc.edu", "demo123");
			if (retrievedUser.getEmail().equals(user.getEmail())) {
				assertTrue(false);
			}
		} catch (HibernateException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
		try {
			UserService.getInstance().createUser(user);
		} catch (HibernateException e) {
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(false);
		}
		assertTrue(true);
	}

	public void getUser() {
		try {
			User retrievedUser = UserService.getInstance().getUser("kingale1@uncc.edu", "demo123");
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void searchUser() {
		try {
			User retrievedUser = UserService.getInstance().getUser("kingale1@uncc.edu", "demo123");
			if (retrievedUser != null) {
				List<User> users = UserService.getInstance().getUsers(retrievedUser.getEmail());
				if (users.get(0).equals(retrievedUser)) {
					assertTrue(true);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createGroup() throws Exception {
		Group group = new Group();
		group.setName("grp1");
		List<User> users = new ArrayList<User>();

		users.addAll(UserService.getInstance().getUsers("000000000"));
		group.setMembers(new HashSet<User>(users));
		UserService.getInstance().createGroup(group);

		Group g = UserService.getInstance().getGroup(group.getGroupId());

	}
}
