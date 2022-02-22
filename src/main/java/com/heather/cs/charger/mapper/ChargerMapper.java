package com.heather.cs.charger.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChargerMapper {
	List<String> selectManager(long categoryId);
}
