package com.heather.cs.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.heather.cs.code.mapper.CodeMapper;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserMapper userMapper;
	private final CodeMapper codeMapper;

	@Transactional
	public void registerUser(User user) {
		if(userMapper.selectExistsUserId(user.getId())) {
			throw new IllegalArgumentException("Duplicated Id");
		}

		user.setRole("COUNSELOR");
		user.setStatus("AVAILABLE");
		user.setUseYn("Y");
		user.setCreatorId(user.getId());
		user.setModifierId(user.getId());

		userMapper.insertUser(user);
		userMapper.insertUserHistory(user.getId());
	}

	// Iteration-2
	public void changeTheCounselorStatus(String userId, String state) {
		if(!userMapper.selectExistsUserId(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The userId does not exist : userId = " + userId);
		}

		String userRole = userMapper.selectUserRole(userId);
		if(!userRole.equals("COUNSELOR")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user is not a counselor : userId = " + userId);
		}

		String GROUP_CODE_OF_COUNSELOR = "COUNSELOR_STATUS";
		String groupCode = codeMapper.selectGroupCode(state);
		if(!groupCode.equals(GROUP_CODE_OF_COUNSELOR)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The state value that a counselor cannot have : state = " + state);
		}

		User user = userMapper.selectUser(userId);
		String status = user.getStatus();
		if(status.equals(state)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "The counselor's status is already " + state);
		}

		user.setStatus(state);
		userMapper.updateStatus(user);
		userMapper.insertUserHistory(userId);

	}

}
