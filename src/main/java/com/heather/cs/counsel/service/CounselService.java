package com.heather.cs.counsel.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.heather.cs.category.mapper.CategoryMapper;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.mapper.CounselMapper;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselMapper counselMapper;
	private final CategoryMapper categoryMapper;
	private final UserMapper userMapper;

	public void registerCounsel(Counsel counsel) {
		if(!categoryMapper.selectExistsCategory(counsel.getCategoryId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Category Id : " + counsel.getCategoryId());
		}

		counsel.setCreatorId("SYSTEM");
		counsel.setModifierId("SYSTEM");
		counsel.setStatus("OK");

		// 상담원 배정 로직 : assignCounsel
		// 1. 카테고리에 속한 매니저 알아내기
		// 2. 해당 카테고리에 상담 가능한 상담원 알아내기
		// 2-1. 가능한 상담원이 없으면 null로 그냥 두기
		// 3. 현재 상담건이 적은 사람
		// 4. 이름 사전 앞에 오는 순으로 배정

		counselMapper.insertCounsel(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}

	public void assignCounsel() {
		// 0. 상담원이 배정되지 않은 문의들 가져오기 : getCounselsWithoutCounselor
		// 1. 카테고리에 속한 매니저 알아내기
		// 2. 해당 카테고리에 상담 가능한 상담원 알아내기
		// 2-1. 가능한 상담원이 없으면 null로 그냥 두기
		// 3. 현재 상담건이 적은 사람
		// 4. 이름 사전 앞에 오는 순으로 배정

	}

	public int getCounselsWithoutCounselor(long categoryId, String userId) {
		if(!userMapper.selectExistsUserId(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist : userId = " + userId);
		}

		List<String> managerList = categoryMapper.selectManager(categoryId);
		managerList.stream()
			.filter(x -> x.equals(userId))
			.findFirst()
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User Not Authorized for categoryId : " + categoryId + ", userId : " + userId));

		return counselMapper.selectCounselsWithoutCounselor(categoryId);
	}
}
