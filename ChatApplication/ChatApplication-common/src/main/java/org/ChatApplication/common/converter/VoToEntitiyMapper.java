package org.ChatApplication.common.converter;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;

public class VoToEntitiyMapper {

	public static User userToUserVo(UserVO u) {
		User user = new User();
		user.setEmail(u.getEmail());
		user.setFirstName(u.getFirstName());
		user.setId(u.getId());
		user.setLastName(u.getLastName());
		user.setNinerId(u.getNinerId());
		user.setPassword(u.getPassword());
		return user;
	}

}
