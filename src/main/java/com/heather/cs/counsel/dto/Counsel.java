package com.heather.cs.counsel.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.heather.cs.code.CounselStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("counsel")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Counsel {
	private Long id;

	@NotNull(message = "The categoryId field is mandatory")
	private Long categoryId;
	private String chargerId;

	@NotBlank(message = "The title field is mandatory")
	@Size(max = 20, message = "Title must be 20 characters or less")
	private String title;

	@NotBlank(message = "The content field is mandatory")
	@Size(max = 10000, message = "Title must be 10000 characters or less")
	private String content;

	@NotBlank(message = "The customerName field is mandatory")
	@Size(max = 25, message = "Title must be 25 characters or less")
	private String customerName;

	@NotBlank(message = "The customerEmail field is mandatory")
	@Size(max = 25, message = "Title must be 25 characters or less")
	private String customerEmail;

	private CounselStatus status;
	private String creatorId;
	private String modifierId;

}
