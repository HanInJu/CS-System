package com.heather.cs.answer.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heather.cs.answer.dto.Answer;
import com.heather.cs.answer.mapper.AnswerMapper;
import com.heather.cs.code.CounselStatus;
import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.mapper.CounselMapper;
import com.heather.cs.counsel.service.CounselService;
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerMapper answerMapper;
	private final CounselMapper counselMapper;

	private final CounselService counselService;

	// 여기서 transactional 하면 propagation Propagation.REQUIRED; 이게 default라서... propagation 알아보기!!!!!!!
	// transactional 동작하지 않는 경우!
	public void registerAnswerForCounsel(Answer answer, User user) {
		long counselId = answer.getCounselId();
		String counselorId = user.getId();
		Counsel counsel = getCounselChargedCounselor(counselId, counselorId);
		if(counsel == null) {
			throw new IllegalArgumentException("Not Privileged User for Counsel. counselId : " + counselId);
		}
		answer.setCreatorId(counselorId);
		answer.setModifierId(counselorId);
		registerAnswer(answer);
		// 39번째 하다가 예외 발생할 경우 정확성이 깨짐
		counselService.updateCounselStatus(counsel, counselorId, CounselStatus.COMPLETED);
		// 서비스에서 서비스로 호출하지 않는 편인데, 상휘->하위로만 autowired 해야 하지 않을까?
		// 왜냐면 생성자주입방식을 원하는 이유가 상호참조를 막기 위한 것 (autowired 상호참조 일어남)
		// 그래서 service를 부르기보다는
		// answerService의 메서드로 만들어서 하면 된다(counselMapper를 가져오는 한이 있더라도)
		// counselService는 객체에서 호출한 거라 Transactional 탐지한다.
	}

	public Counsel getCounselChargedCounselor(long counselId, String counselorId) {
		Map<String, String > map = new HashMap<>();
		map.put("counselId", String.valueOf(counselId));
		map.put("counselorId", counselorId);
		return counselMapper.selectCounselChargedCounselor(map);
	}

	@Transactional // 52번째 중에 예외가 발생될 경우 프록시 패턴 때문에 여기 transactional 적용 안됨
	// 호출한 메서드 내부에서 예외가 발생할 경우 프록시가 탐지를 못한다... -> 그래서 Transactional
	public void registerAnswer(Answer answer) {
		answerMapper.insertAnswer(answer);
		answerMapper.insertAnswerInHistory(answer.getId()); // answerMapper 내부에서 함수 호출하는 경우 프록시가 annotation 탐지X
	}

}
