package org.ChatApplication.data.entity;

import java.io.IOException;

import org.ChatApplication.common.converter.ByteToEntityConverter;
import org.ChatApplication.common.converter.EntityToByteConverter;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public class EntityTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		testPackUser();
		testPackGroup();
		testPackGroupVO();
	}

	
	
	private static void testPackGroupVO() {
		GroupVO groupVO;		
		
		
	}

	
	
	private static void testPackGroup() {
		// TODO Auto-generated method stub
		Group group = new Group();
		
		
	}

	
	
	private static void testPackUser() {
User user = new User();
		User unpackedUser = null;
		user.setId(1);
		user.setFirstName("John");
		user.setLastName("Doe");
		user.setNinerId("800999999");
		user.setEmail("johndoe@uncc.edu");
		user.setPassword("Root@123");
		
		System.out.println("Before packing...");
		System.out.println(user.getId()+ " " + user.getNinerId()+" " + user.getFirstName()+" " +user.getLastName()+" "+ user.getEmail()+" "+user.getPassword());
		
		
		try {
			byte[] user_byte = EntityToByteConverter.getInstance().getBytes(user);
	//		unpackedUser = ByteToEntityConverter.getInstance().getUser(user_byte);
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
		
		System.out.println("After Unpacking...");
		System.out.println(unpackedUser.getId()+ " " + unpackedUser.getNinerId()+" " + unpackedUser.getFirstName()+" " +unpackedUser.getLastName()+" "+ unpackedUser.getEmail()+" "+unpackedUser.getPassword());
		
	}

}
