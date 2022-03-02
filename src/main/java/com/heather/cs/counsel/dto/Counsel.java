package com.heather.cs.counsel.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

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
	private String title;

	@NotBlank(message = "The content field is mandatory")
	private String content;

	@NotBlank(message = "The customerName field is mandatory")
	private String customerName;

	@NotBlank(message = "The customerEmail field is mandatory")
	private String customerEmail;
	private String status;
	private String creatorId;
	private String modifierId;
}
