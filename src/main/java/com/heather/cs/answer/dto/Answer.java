package com.heather.cs.answer.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Alias("answer")
public class Answer {
	private Long id;

	@NotNull(message = "The counselId field is mandatory")
	private Long counselId;

	@NotBlank(message = "The content field is mandatory")
	private String content;
	private String creatorId;
	private String modifierId;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
}
