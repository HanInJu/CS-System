package com.heather.cs.counsel.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.counsel.dto.Counsel;

@Mapper
public interface CounselMapper {
	void insertCounsel(Counsel counsel);
	void insertCounselHistory(long counselId);
	List<Counsel> selectUnassignedCounselList(String managerId);
	void updateCounselCharger(Counsel counsel);
	Counsel selectAssignedCounsel(Map<String, String> map);
	void updateCounselStatus(Counsel counsel);
}
