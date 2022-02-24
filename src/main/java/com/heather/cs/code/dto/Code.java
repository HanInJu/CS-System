package com.heather.cs.code.dto;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("Code")
public class Code {
	private String code;
	private String groupCode;
	private String name;
}
