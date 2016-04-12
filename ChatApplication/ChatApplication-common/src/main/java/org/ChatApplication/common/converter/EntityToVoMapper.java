package org.ChatApplication.common.converter;

import java.util.ArrayList;
import java.util.List;

import org.ChatApplication.data.entity.User;
import org.ChatApplication.data.entity.UserVO;

public class EntityToVoMapper {

	public static UserVO userToUserVo(User u) {
		UserVO userVO = new UserVO();
		userVO.setEmail(u.getEmail());
		userVO.setFirstName(u.getFirstName());
		userVO.setId(u.getId());
		userVO.setLastName(u.getLastName());
		userVO.setNinerId(u.getNinerId());
		userVO.setPassword(null);
		return userVO;
	}

	public static List<UserVO> userToUserVo(List<User> users) {
		List<UserVO> userVOs = new ArrayList<UserVO>();
		for (User u : users) {
			userVOs.add(userToUserVo(u));
		}
		return userVOs;
	}

}
