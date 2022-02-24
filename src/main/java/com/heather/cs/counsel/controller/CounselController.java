package com.heather.cs.counsel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.counsel.dto.Counsel;
import com.heather.cs.counsel.dto.CounselManager;
import com.heather.cs.counsel.service.CounselService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CounselController {

	private final CounselService counselService;

	@PostMapping("/counsel")
	public void registerCounsel(@RequestBody Counsel counsel) {
		counselService.registerCounsel(counsel);
	}

	// GET 매핑으로 해도 충분하지 않을까... 매니ㅓ id는 쿠키에서 꺼내야지
	// 쿠키를 만들었으면 쿠키를 써야지?
	@PostMapping("/counsel/assignment")
	public void assignCounsels(@RequestBody CounselManager counselManager) {
		counselService.assignCounsels(counselManager);
	}

	// autority라는 표현이...?
	// 이미 로그인 된 상태에서 조회해야 하기 때문에 쿠키에 있는 아이디로 조회하는 게 맞다. 값 꺼내서
	// 매니저만 사용할 수 있으니까 컨트롤러에서 권한체크가 있었어야 함
	@GetMapping("/counsel/category/{categoryId}/authority")
	public int getCounselsWithoutCounselor(@PathVariable long categoryId, @RequestParam String managerId) {
		return counselService.getCounselsWithoutCounselor(categoryId, managerId).size();
		// List로 가져오는 거 불필요하다 COUNT로 SQL에서 셀 수 있는데 왜 여기까지 넘어왔는지
		// 성능 조
	}
}
