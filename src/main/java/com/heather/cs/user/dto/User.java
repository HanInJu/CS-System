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
	private String role = "COUNSELOR";
	private String status = "AVAILABLE";
	private String userYn = "Y";
	private String creatorId;
	private String modifierId;
}
