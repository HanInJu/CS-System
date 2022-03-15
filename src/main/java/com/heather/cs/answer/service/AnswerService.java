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
import com.heather.cs.user.dto.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerMapper answerMapper;
	private final CounselMapper counselMapper;

	@Transactional
	public void registerAnswerForCounsel(Answer answer, User user) {
		long counselId = answer.getCounselId();
		String counselorId = user.getId();

		Counsel counsel = getCounselChargedCounselor(counselId, counselorId);
		if (counsel == null) {
			throw new IllegalArgumentException("Not Privileged User for Counsel. counselId : " + counselId);
		}

		setCreatorAndModifier(answer, counselorId);
		registerAnswer(answer);

		changeStatusAndModifier(counsel, CounselStatus.COMPLETED, counselorId);
		updateCounsel(counsel);
	}

	public Counsel getCounselChargedCounselor(long counselId, String counselorId) {
		Map<String, String> map = new HashMap<>();
		map.put("counselId", String.valueOf(counselId));
		map.put("counselorId", counselorId);
		return counselMapper.selectCounselChargedCounselor(map);
	}

	public void setCreatorAndModifier(Answer answer, String userId) {
		answer.setCreatorId(userId);
		answer.setModifierId(userId);
	}

	public void registerAnswer(Answer answer) {
		answerMapper.insertAnswer(answer);
		answerMapper.insertAnswerInHistory(answer.getId());
	}

	public void changeStatusAndModifier(Counsel counsel, CounselStatus status, String userId) {
		counsel.setModifierId(userId);
		counsel.setStatus(status.toString());
	}

	public void updateCounsel(Counsel counsel) {
		counselMapper.updateCounselStatus(counsel);
		counselMapper.insertCounselHistory(counsel.getId());
	}

}
