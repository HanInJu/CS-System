package com.heather.cs.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.user.dto.User;

@Mapper
public interface UserMapper {
	boolean selectExistsUserId(String userId);
	void insertUser(User user);
	void insertUserHistory(String userId);
	void updateUserStatus(User user);
	User selectActiveUser(String userId);
	List<User> selectStatusOnUser();
	void updateUserStatusToOff(String userId);
}
