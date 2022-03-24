package com.heather.cs.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;
import com.heather.cs.user.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TestUserService {

	@InjectMocks
	private UserService userService;

	@Mock
	private UserMapper userMapper;

	@Test
	@DisplayName("Test to register User")
	public void testRegisterUser() { //분기 TC
		// given
		User user = new User();
		user.setId("heather");
		user.setPassword("qwerty");

		// when
		Mockito.when(userMapper.selectExistsUserId(user.getId())).thenReturn(false);
		Mockito.doNothing().when(userMapper).insertUser(user);
		Mockito.doNothing().when(userMapper).insertUserHistory(user.getId());

		// then
		Assertions.assertDoesNotThrow(() -> userService.registerUser(user));
	}
}
