package com.heather.cs.user.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.heather.cs.user.dto.User;
import com.heather.cs.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/user")
	public void registerUser(@RequestBody User user) {
		userService.registerUser(user);
	}

	// 권한을 조회하는 느낌인데... (URI)
	@GetMapping("/user/authorization")
	public void signIn(HttpServletResponse response, @RequestParam String id, @RequestParam String password) {
		// body로 받을 수 있도록 POST로 받아주자
		// 쿠키가 있는 유저가 또 로그인할 경우 어떻게 디는지?
		// useYn이 Y가 아니면 로그인 안되어야 함 (만약 큐키가 있으면 쿠키 자체가 활성유저임을 확인해주는 거니까) : 엄밀히 ㅁ라하면 활서유저를 다 검사해야 함
		String validId = userService.signIn(id, password); // id를 이미 갖고 있는데 왜 또 리턴...
		Cookie userIdCookie = new Cookie("userIdCookie", validId); // string은 상수로 의미있는 값으로 변수를 만들면 좋다.
		// 자바8부터는 따로 상수를 관리한다. 계속 새로 만들지는 않지만, 그래도 상수로 빼기
		userIdCookie.setMaxAge(3600); // 상수로 3600, 운영시스템에서는 1시간이 짧음 좀 길게 해줘도 괜찮음
		response.addCookie(userIdCookie);
	}

	// 가능->불가능, 불가능->가능 두 개가 요구사항이었는데
	// 상태코드를 클라이언트에 노출시키는 것을 최소화 (변경될 경우 클라이언트도 변경해야 하기 때문에)
	// 그래서 두 기능을 두 메서드로 분리하면 좋을 것 같다.
	// 관사는 빼기, 복수 표현은 쓰지만 관사는 잘 안 쓰는 편
	@PatchMapping("/user/counselor/{userId}/status")
	public void changeTheCounselorStatus(@PathVariable String userId, @RequestParam String state) {
		userService.changeTheCounselorStatus(userId, state);
	}

}
