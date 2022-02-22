package com.heather.cs.counsel.service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.heather.cs.category.mapper.CategoryMapper;
import com.heather.cs.charger.dto.Charger;
import com.heather.cs.charger.mapper.ChargerMapper;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.dto.CounselManager;
import com.heather.cs.counsel.mapper.CounselMapper;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselMapper counselMapper;
	private final CategoryMapper categoryMapper;
	private final ChargerMapper chargerMapper;
	private final UserMapper userMapper;

	public void registerCounsel(Counsel counsel) {
		if(!categoryMapper.selectExistsCategory(counsel.getCategoryId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Category Id : " + counsel.getCategoryId());
		}
		if(categoryMapper.selectExistsChildCategory(counsel.getCategoryId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The category is NOT a lowest category : categoryId = " + counsel.getCategoryId());
		}

		Charger counselor = chargerMapper.selectCounselor(counsel.getCategoryId());
		counsel.setChargerId(counselor.getUserId());
		counsel.setCreatorId("SYSTEM");
		counsel.setModifierId("SYSTEM");
		counsel.setStatus("OK");

		counselMapper.insertCounsel(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}

	public void assignCounsels(CounselManager counselManager) {
		List<Counsel> counselList = getCounselsWithoutCounselor(counselManager.getCategoryId(), counselManager.getManagerId());
		List<Charger> counselorList = chargerMapper.selectCounselors(counselManager.getCategoryId());

		if(counselorList.size() == 0) {
			return;
		}

		PriorityQueue<Charger> counselorQueue = new PriorityQueue<>(counselorList);
		for (Counsel currentCounsel : counselList) {
			if (counselorQueue.isEmpty()) {
				throw new IllegalStateException("Cast Error : Cannot convert List to Queue");
			}
			Charger currentCharger = counselorQueue.poll();
			currentCounsel.setChargerId(currentCharger.getUserId());

			currentCharger.setNumberOfCounsel(currentCharger.getNumberOfCounsel() + 1);
			counselorQueue.add(currentCharger);
		}

		for(Counsel counsel : counselList) {
			counsel.setModifierId(counselManager.getManagerId());
			counselMapper.updateCounsel(counsel);
			counselMapper.insertCounselHistory(counsel.getId());
		}

	}

	public List<Counsel> getCounselsWithoutCounselor(long categoryId, String managerId) {
		if(!userMapper.selectExistsUserId(managerId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist : managerId = " + managerId);
		}

		List<String> managerList = chargerMapper.selectManagers(categoryId);
		managerList.stream()
			.filter(x -> x.equals(managerId))
			.findFirst()
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User Not Authorized for categoryId : " + categoryId + ", managerId : " + managerId));

		return counselMapper.selectCounselsWithoutCounselor(categoryId); // counselId, categoryId
	}
}
