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
		counsel.setStatus("OK"); // 상담의 상태코드는 할당/미할당/답변완료 이런 식으로 가져가야 함

		// 어떤 곳에는 Transactional 하고 어떤 곳에는 안하고 하는데 한 프로젝트 내에서는 통일성을 줘야지
		// 이력을 Transactional로 묶는 게 나은 이유 : 추적하기가 힘들기 때문
		// 이력이 누락되면 무슨 일이 생겼는지 파악이 안되니까 보통 Transactional로 묶는 편임
		// 이것 때문에 트래픽이 막히면 아키텍처를 다시 잡아야지 이런 소소한 부분은 걸어주는 게 맞음
		counselMapper.insertCounsel(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}

	public void assignCounsels(CounselManager counselManager) { // modifier가 매니저
		List<Counsel> counselList = getCounselsWithoutCounselor(counselManager.getCategoryId(), counselManager.getManagerId());
		// 활성화된 상담원을 선택했다는 의미를 메서드에 못담음 (selectCounselors)
		List<Charger> counselorList = chargerMapper.selectCounselors(counselManager.getCategoryId());

		if(counselorList.size() == 0) {
			return; // 그 냥 리턴을 해 주는 게 맞나...
			// 미분배 된 상담개수가 줄지 않았으니까 알아서 판단했겠지만, 거기에 대한 메시지는 줬어야지
			// 현재 상담원이 없다거나 하는 메시지를 줬어야지
		}

		// 생성자에서 Comparator를 따로 선언해서 주는 게 더 나을 것 같음
		// charger는 지금 DTO로 쓰고 있는데 거기에 comparator를 구현하는 게 예쁜 모습이 아님
		PriorityQueue<Charger> counselorQueue = new PriorityQueue<>(counselorList);
		// 덩이씩 되어 있는 건 적절하게 메서드로 빼는 게 좋지 않나?
		for (Counsel currentCounsel : counselList) {
			// 58번째랑 중복되는 것 같은데? 만약 메서드로 뺐을 때에는 필요할 수도 있지만...
			// 그리고 이게 왜 for문 안에 있어? 넣고 빼고 한다고 생각해도 하나 넣고 하나 빼는데 빌 일은 없지. for문 앞에서 미리 체크했어야 하는 게 맞아 이렇게 되면 for 돌 때마다 체크하니까
			if (counselorQueue.isEmpty()) {
				throw new IllegalStateException("Cast Error : Cannot convert List to Queue");
			}
			Charger currentCharger = counselorQueue.poll();
			currentCounsel.setChargerId(currentCharger.getUserId());

			currentCharger.setNumberOfCounsel(currentCharger.getNumberOfCounsel() + 1); // setter 보다는 내가 가진 상담 수를 하나 올리겠다는 메서드로 뺐어야 함
			counselorQueue.add(currentCharger);
		}

		for(Counsel counsel : counselList) {
			counsel.setModifierId(counselManager.getManagerId());
			// 여기서 상담 건이 여러 개인데 10개 중 4개만 될 수도 있는데 왜 Transactional 안 넣음?
			counselMapper.updateCounsel(counsel);
			// 이거 하나씩 도는 게 아니라  다량으로 넣을 때는 ㅡyBatis에서 for-each 돌릴 수 있음
			counselMapper.insertCounselHistory(counsel.getId());
		}

	}

	public List<Counsel> getCounselsWithoutCounselor(long categoryId, String managerId) {
		// 여기도 컨트롤러에서 쿠키로 체크했어야 (보통 쿠키에 대한 유효성 검사를 앞단에서 interceptor 나 그런 걸로 해야 함)
		/// 체크하면 뒤 메서드에서는 믿고 진행
		if(!userMapper.selectExistsUserId(managerId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist : managerId = " + managerId);
		}
		// 카테고리에 이 매니저가 있는지 SQL로 체크하는 게 더 깔끔하지
		List<String> managerList = chargerMapper.selectManagers(categoryId);
		managerList.stream()
			.filter(x -> x.equals(managerId))
			.findFirst()
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "The User Not Authorized for categoryId : " + categoryId + ", managerId : " + managerId));

		return counselMapper.selectCounselsWithoutCounselor(categoryId); // counselId, categoryId
		// 여기 List X
	}
}
