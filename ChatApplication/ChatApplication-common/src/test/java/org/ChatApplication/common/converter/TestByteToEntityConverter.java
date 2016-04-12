/**
 * 
 */
package org.ChatApplication.common.converter;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;

import junit.framework.TestCase;

/**
 * @author Devdatta
 *
 */
public class TestByteToEntityConverter extends TestCase {

	UserVO userVO = new UserVO();
	
	
	public void testUserConversion() {
		String email = "asd@gmail.com" ;
		String fname = "asd";
		String passwd = "root";
		String ninerId = "000000000";
		int id = 123123;
		
		userVO  = new UserVO();
		userVO.setEmail(email);
		userVO.setFirstName(fname);
		userVO.setId(id);
		userVO.setNinerId(ninerId);
		userVO.setPassword(passwd);
		
		//EntityToByteConverter.getInstance().getBytes(userVO);
	}
	
}
