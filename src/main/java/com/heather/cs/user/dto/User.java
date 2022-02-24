package com.heather.cs.user.dto;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
	private String id;
	private String name;
	private String password;
	private String role;
	private String status;
	private String useYn;
	private String creatorId;
	private String modifierId;
}
