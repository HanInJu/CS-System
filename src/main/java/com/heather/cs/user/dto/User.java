package com.heather.cs.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

	@NotBlank(message = "The id field is mandatory")
	@Size(max = 20, message = "Title must be 20 characters or less")
	private String id;

	@NotBlank(message = "The name field is mandatory")
	@Size(max = 20, message = "Title must be 20 characters or less")
	private String name;

	@NotBlank(message = "The message field is mandatory")
	@Size(max = 20, message = "Title must be 20 characters or less")
	private String password;
	private String role;
	private String status;
	private String useYn;
	private String creatorId;
	private String modifierId;
}
