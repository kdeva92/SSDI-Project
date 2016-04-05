package org.ChatApplication.common.converter;

import java.io.IOException;
import java.util.List;

import org.ChatApplication.data.entity.User;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;

/**
 * 
 * @author Komal
 *
 */
public class ByteToEntityConverter {

	static ByteToEntityConverter instance;

	public static ByteToEntityConverter getInstance() {
		if (instance == null) {
			instance = new ByteToEntityConverter();
		}
		return instance;
	}

	public User getUser(byte[] user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(user, User.class);
	}

	public User getUser(String user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(user, User.class);
	}

	public List<User> getUsers(byte[] users) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(users, TypeFactory.collectionType(List.class, User.class));
	}

}
