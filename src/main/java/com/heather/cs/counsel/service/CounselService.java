package com.heather.cs.counsel.service;

import org.springframework.stereotype.Service;

import com.heather.cs.category.mapper.CategoryMapper;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.mapper.CounselMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselMapper counselMapper;
	private final CategoryMapper categoryMapper;

	public void registerCounsel(Counsel counsel) {
		if(!categoryMapper.selectExistsCategory(counsel.getCategoryId())) {
			throw new IllegalArgumentException("Invalid Category Id : " + counsel.getCategoryId());
		}

		counsel.setCreatorId("SYSTEM");
		counsel.setModifierId("SYSTEM");
		counsel.setStatus("OK");
		counselMapper.insertCounsel(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}
}
