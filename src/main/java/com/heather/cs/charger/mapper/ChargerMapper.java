package com.heather.cs.charger.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.charger.dto.Charger;

@Mapper
public interface ChargerMapper {
	List<String> selectManagers(long categoryId);
	List<Charger> selectCounselors(long categoryId);
	Charger selectCounselor(long categoryId);
}
