package com.heather.cs.counsel.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.counsel.dto.Counsel;

@Mapper
public interface CounselMapper {
	void insertCounsel(Counsel counsel);
	void insertCounselHistory(long counselId);
	List<Counsel> selectCounselsWithoutCounselor(long categoryId);
	void updateCounsel(Counsel counsel);
}
