package com.heather.cs.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heather.cs.code.CounselorStatus;
import com.heather.cs.code.ManagerStatus;
import com.heather.cs.code.UserIdentifier;
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
		user.setStatus(CounselorStatus.AVAILABLE.toString());
		user.setUseYn("Y");
		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user.getId());
	}

	public boolean isValidUser(String userId, String password) {
		User user = userMapper.selectActiveUser(userId);
		if (!password.equals(user.getPassword())) {
			throw new IllegalArgumentException("Password is not correct : userId = " + userId);
		}
		return true;
	}

	public void changeStatusOn(User user) {
		if (hasManagerPrivileges(user.getId())) {
			changeManagerStatusOn(user);
		}
		changeCounselorStatusOn(user);
	}

	public void changeStatusOff(User user) {
		if (hasManagerPrivileges(user.getId())) {
			changeManagerStatusOff(user);
		}
		changeCounselorStatusOff(user);
	}

	public boolean hasManagerPrivileges(String userId) {
		User user = userMapper.selectActiveUser(userId);
		return user.getRole().equals(UserIdentifier.MANAGER.toString());
	}

	@Transactional
	public void changeCounselorStatusOn(User user) {
		String status = user.getStatus();
		if (status.equals(CounselorStatus.AVAILABLE.toString())) {
			throw new IllegalStateException("The counselor's status is already ON");
		}
		status = CounselorStatus.AVAILABLE.toString();
		user.setStatus(status);
		userMapper.updateUserStatus(user);
		userMapper.insertUserHistory(user.getId());
	}

	@Transactional
	public void changeCounselorStatusOff(User user) {
		String status = user.getStatus();
		if (status.equals(CounselorStatus.UNAVAILABLE.toString())) {
			throw new IllegalStateException("The counselor's status is already OFF");
		}
		status = CounselorStatus.UNAVAILABLE.toString();
		user.setStatus(status);
		userMapper.updateUserStatus(user);
		userMapper.insertUserHistory(user.getId());
	}

	@Transactional
	public void changeManagerStatusOn(User user) {
		String status = user.getStatus();
		if (status.equals(ManagerStatus.NORMAL.toString())) {
			throw new IllegalStateException("The manager's status is already ON");
		}
		status = ManagerStatus.NORMAL.toString();
		user.setStatus(status);
		userMapper.updateUserStatus(user);
		userMapper.insertUserHistory(user.getId());
	}

	@Transactional
	public void changeManagerStatusOff(User user) {
		String status = user.getStatus();
		if (status.equals(ManagerStatus.SUSPENDED.toString())) {
			throw new IllegalStateException("The manager's status is already OFF");
		}
		status = ManagerStatus.SUSPENDED.toString();
		user.setStatus(status);
		userMapper.updateUserStatus(user);
		userMapper.insertUserHistory(user.getId());
	}

}
