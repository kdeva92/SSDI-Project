/**
 * 
 */
package org.ChatApplication.common.converter;

import java.io.IOException;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

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
		
		byte[] user;
		try {
			user = EntityToByteConverter.getInstance().getBytes(userVO);
			UserVO retUser = ByteToEntityConverter.getInstance().getUser(user);
			
			if(retUser.getEmail().equals(userVO.getEmail())&& retUser.getFirstName().equals(userVO.getFirstName())&& retUser.getId()==userVO.getId()&& retUser.getNinerId().equals(userVO.getNinerId())&& retUser.getPassword().equals(userVO.getPassword()))
				assertTrue(true);
			else 
				assertTrue(false);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
}
