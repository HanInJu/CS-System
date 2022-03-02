package com.heather.cs.user.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heather.cs.code.dto.UserIdentifier;
import com.heather.cs.code.dto.UserStatus;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;

	@Transactional
	public void registerUser(User user) {
		if (userMapper.selectExistsUserId(user.getId())) {
			throw new IllegalArgumentException("Duplicated Id : id = " + user.getId());
		}

		user.setRole(UserIdentifier.COUNSELOR.toString());
		user.setStatus(UserStatus.AVAILABLE.toString());
		user.setUseYn("Y");
		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user.getId());
	}

	public boolean isValidUser(String userId, String password) {
		User user = userMapper.selectActiveUser(userId);
		if (!password.equals(user.getPassword())) {
			throw new IllegalStateException("Password is not correct : userId = " + userId);
		}
		return true;
	}

	@Transactional
	public void changeStatusOn(String userId) {
		User user = userMapper.selectActiveUser(userId);
		String status = user.getStatus();
		if (status.equals(UserStatus.AVAILABLE.toString())) {
			throw new IllegalStateException("The counselor's status is already ON");
		}

		status = UserStatus.AVAILABLE.toString();
		Map<String, String> map = new HashMap<>();
		map.put("userId", userId);
		map.put("status", status);
		userMapper.updateStatus(map);
		userMapper.insertUserHistory(userId);
	}

	@Transactional
	public void changeStatusOff(User user) {
		String status = user.getStatus();
		if (status.equals(UserStatus.UNAVAILABLE.toString())) {
			throw new IllegalStateException("The counselor's status is already OFF");
		}
		status = UserStatus.UNAVAILABLE.toString();
		Map<String, String> map = new HashMap<>();
		map.put("userId", user.getId());
		map.put("status", status);
		userMapper.updateStatus(map);
		userMapper.insertUserHistory(user.getId());
	}

	public void checkManagerPrivileges(String userId) {
		User user = userMapper.selectActiveUser(userId);
		if(!user.getRole().equals(UserIdentifier.MANAGER.toString())) {
			throw new IllegalArgumentException("No Permission : userId = " + userId);
		}
	}
	
	public void checkCookie(Cookie cookie) {
		if(cookie == null) {
			throw new IllegalArgumentException("No LogIn Information");
		}
	}

}
