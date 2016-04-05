package org.ChatApplication.common.converter;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 
 * @author Komal
 *
 */
public class EntityToByteConverter {
	static EntityToByteConverter instance;

	public static EntityToByteConverter getInstance() {
		if (instance == null) {
			instance = new EntityToByteConverter();
		}
		return instance;
	}

	public String getJsonString(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

	public byte[] getBytes(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsBytes(object);
	}
}
