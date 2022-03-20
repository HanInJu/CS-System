package com.heather.cs.statistics.dto;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Alias("statistics")
@NoArgsConstructor
@AllArgsConstructor
public class Statistics { // 두 쿼리가 통계인 건 맞지만 페이지가 다르니까 다르게 분리했어야 함(스펙이 복잡해질수록 분리)
	// 보통 조건 많은 통계들은 컨트롤러부터 dto 까지 다 결합도가 엄청 높다. 그럴 수 밖에 없음...
	// 그러니까 분리했어야 한다.

	@JsonInclude(JsonInclude.Include.NON_DEFAULT)
	private int registeredCounsels;

	private int completedCounsels;

	private int delayedCounsels;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String date;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private int counselsProcessedNormally;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String counselorId;

}
