package com.heather.cs.configuration.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.heather.cs.code.UserIdentifier;
import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ManagerPrivilegeInterceptor implements HandlerInterceptor {

	private final UserMapper userMapper;
	private final String AUTHENTICATED_COOKIE = "userIdCookie";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String userId = getUserId(request);
		UserIdentifier userRole = getUserRole(userId);
		if (userRole != UserIdentifier.MANAGER) {
			throw new IllegalArgumentException("NOT PRIVILEGED USER (userId : " + userId + ")");
		}
		return true;
	}

	public String getUserId(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, AUTHENTICATED_COOKIE);
		return cookie.getValue();
	}

	public UserIdentifier getUserRole(String userId) {
		User user = userMapper.selectActiveUser(userId); // 정상 로그인 -> 쿠키 발행 -> 쿠키 있음이 확인된 것이므로 유효한 user
		return UserIdentifier.valueOf(user.getRole());
	}

}
