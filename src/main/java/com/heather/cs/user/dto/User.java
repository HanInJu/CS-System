package com.heather.cs.user.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("user")
public class User {
	private String id;
	private String name;
	private String password;
	private String role;
	private String status;
	private String userYn;
	private String creatorId;
	private String modifierId;
}
