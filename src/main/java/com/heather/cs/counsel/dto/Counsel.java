package com.heather.cs.counsel.dto;

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
	private Long categoryId;
	private String chargerId;
	private String title;
	private String content;
	private String customerName;
	private String customerEmail;
	private String status;
	private String creatorId;
	private String modifierId;
}
