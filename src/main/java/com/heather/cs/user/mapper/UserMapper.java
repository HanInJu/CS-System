package com.heather.cs.user.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.user.dto.User;

@Mapper
public interface UserMapper {
	boolean selectExistsUserId(String userId);
	void insertUser(User user);
	void insertUserHistory(String userId);
	void updateStatus(Map<String, String> map);
	User selectActiveUser(String userId);
}
