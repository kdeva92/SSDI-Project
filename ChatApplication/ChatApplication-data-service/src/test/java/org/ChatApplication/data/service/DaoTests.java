package org.ChatApplication.data.service;

import org.ChatApplication.data.entity.User;

import junit.framework.TestCase;

/**
 * 
 * @author Komal
 *
 */
public class DaoTests extends TestCase {

	public DaoTests() {
		// TODO Auto-generated constructor stub
	}
	public void createUser() {
		User user = new User();
		user.setEmail("kigale@uncc.edu");
		user.setFirstName("Komal");
		user.setLastName("Ingale");
		user.setPassword("demo123");
		
		try {
			UserService.getInstance().createUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
		assertTrue(true);
	}
}
