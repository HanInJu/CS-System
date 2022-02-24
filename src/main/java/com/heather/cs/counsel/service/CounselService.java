package com.heather.cs.counsel.service;

import java.util.List;
import java.util.PriorityQueue;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heather.cs.category.mapper.CategoryMapper;
import com.heather.cs.charger.dto.Charger;
import com.heather.cs.charger.mapper.ChargerMapper;
import com.heather.cs.code.dto.CommonCode;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.mapper.CounselMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselService {

	private final CounselMapper counselMapper;
	private final CategoryMapper categoryMapper;
	private final ChargerMapper chargerMapper;

	@Transactional
	public void registerCounsel(Counsel counsel) {
		if(!categoryMapper.selectExistsCategory(counsel.getCategoryId())) {
			throw new IllegalArgumentException("Invalid Category Id : " + counsel.getCategoryId());
		}
		if(categoryMapper.selectExistsChildCategory(counsel.getCategoryId())) {
			throw new IllegalArgumentException("The category is NOT a lowest category : categoryId = " + counsel.getCategoryId());
		}

		Charger counselor = chargerMapper.selectOneAvailableCounselor(counsel.getCategoryId());
		counsel.setChargerId(counselor.getUserId());
		counsel.setCreatorId("SYSTEM");
		counsel.setModifierId("SYSTEM");
		counsel.setStatus(CommonCode.ASSIGNED.toString());

		counselMapper.insertCounsel(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}

	@Transactional
	public void assignCounsels(String managerId) {
		List<Counsel> counselList = getUnassignedCounselList(managerId);
		List<Charger> counselorList = chargerMapper.selectAvailableCounselorList(managerId);

		if(counselorList.size() == 0) {
			throw new IllegalStateException("There are no Counselors available.");
		}

		PriorityQueue<Charger> counselorQueue = makePriorityQueue(counselorList);
		if (counselorQueue.isEmpty()) {
			throw new IllegalStateException("Cast Error : Cannot convert List to Queue");
		}

		for (Counsel currentCounsel : counselList) {
			Charger currentCharger = counselorQueue.poll();
			assignCounselor(currentCounsel, currentCharger.getUserId());
			changeModifier(currentCounsel, managerId);
			addOneMoreCounsel(currentCharger);
			counselorQueue.add(currentCharger);
		}

		//아래 주석처리된 두 줄은 myBatis foreach 문으로 설정했을 때 실행할 함수인데, 현재 myBatis foreach 방법을 찾을 수 없음
		//xml 파일에서 foreach를 사용해 쿼리를 만들어두었으나, SQLException으로 실행불가 (3시간 구글링해도 해결안됨) : 02.25
		//counselMapper.updateCounselCharger(counselList);
		//counselMapper.insertCounselChargerInHistory(counselList);
		for(Counsel counsel : counselList) {
			counselMapper.updateCounselCharger(counsel);
			counselMapper.insertCounselHistory(counsel.getId());
		}

	}

	public PriorityQueue<Charger> makePriorityQueue(List<Charger> chargerList) {
		PriorityQueue<Charger> counselorQueue = new PriorityQueue<>((charger1, charger2) -> {
			if(charger1.getNumberOfCounsel() < charger2.getNumberOfCounsel()) {
				return -1;
			}
			if(charger1.getNumberOfCounsel() > charger2.getNumberOfCounsel()) {
				return 1;
			}
			return charger1.getUserId().compareTo(charger2.getUserId());
		});
		for(Charger charger : chargerList) {
			counselorQueue.add(charger);
		}

		return counselorQueue;
	}

	public void changeModifier(Counsel counsel, String modifierId) {
		counsel.setModifierId(modifierId);
	}

	public void assignCounselor(Counsel counsel, String counselorId) {
		counsel.setChargerId(counselorId);
	}

	public void addOneMoreCounsel(Charger charger) {
		int numberOfCounsel = charger.getNumberOfCounsel();
		charger.setNumberOfCounsel(numberOfCounsel + 1);
	}

	public List<Counsel> getUnassignedCounselList(String managerId) {
		return counselMapper.selectUnassignedCounselList(managerId);
	}

	public int getCounselsWithoutCharger(String managerId) {
		return chargerMapper.selectCountUnassignedCounsels(managerId);
	}

}
