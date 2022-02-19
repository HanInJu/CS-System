package com.heather.cs.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.user.dto.User;

@Mapper
public interface UserMapper {
	boolean selectExistsUserId(String userId);
	void insertUser(User user);
	void insertUserHistory(User user);
}
