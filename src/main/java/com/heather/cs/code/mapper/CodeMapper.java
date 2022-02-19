package com.heather.cs.code.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMapper {
	String selectGroupCode(String state);
}
