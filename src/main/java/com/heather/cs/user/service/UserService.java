package com.heather.cs.user.service;

import org.springframework.stereotype.Service;

import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;

	public void registerUser(User user) {
		if(userMapper.selectExistsUserId(user.getId())) {
			throw new IllegalArgumentException("Duplicated Id");
		}

		user.setRole("COUNSELOR");
		user.setStatus("AVAILABLE");
		user.setUserYn("Y");
		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user);
	}
}
