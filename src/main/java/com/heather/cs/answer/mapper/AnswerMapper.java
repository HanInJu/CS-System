package com.heather.cs.answer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.heather.cs.answer.dto.Answer;

@Mapper
public interface AnswerMapper {
	void insertAnswer(Answer answer);
	void insertAnswerInHistory(long answerId);
}
