package org.ChatApplication.common.converter;

import java.io.IOException;
import java.util.List;

import org.ChatApplication.data.entity.GroupVO;
import org.ChatApplication.data.entity.UserVO;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.h2.engine.User;

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

	public UserVO getUser(byte[] user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(user, UserVO.class);
	}

	public User getUser(String user) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(user, User.class);
	}

	public List<UserVO> getUsers(byte[] users) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(users, TypeFactory.collectionType(List.class, User.class));
	}


	public GroupVO getGroupVO(byte[] group) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(group, GroupVO.class);
	}
}
