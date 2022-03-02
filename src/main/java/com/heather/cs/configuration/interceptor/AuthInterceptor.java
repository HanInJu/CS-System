package com.heather.cs.configuration.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import com.heather.cs.user.dto.User;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

	private final UserMapper userMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Cookie cookie = WebUtils.getCookie(request, "userIdCookie");
		if (cookie != null) {
			User user = userMapper.selectActiveUser(cookie.getValue());
			request.setAttribute("AUTH_USER", user);
		}

		return cookie != null;
	}
}
