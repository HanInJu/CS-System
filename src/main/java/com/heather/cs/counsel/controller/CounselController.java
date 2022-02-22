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

	@PostMapping("/counsel/assignment")
	public void assignCounsels(@RequestBody CounselManager counselManager) {
		counselService.assignCounsels(counselManager);
	}

	@GetMapping("/counsel/category/{categoryId}/authority")
	public int getCounselsWithoutCounselor(@PathVariable long categoryId, @RequestParam String managerId) {
		return counselService.getCounselsWithoutCounselor(categoryId, managerId).size();
	}
}
