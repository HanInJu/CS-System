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
			throw new IllegalArgumentException("중복된 ID 입니다.");
		}

		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user);
	}
}
