package com.heather.cs.configuration.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final String AUTH_COOKIE = "userIdCookie";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Cookie cookie = WebUtils.getCookie(request, AUTH_COOKIE);
		if(cookie == null) {
			throw new IllegalStateException("NOT AUTHENTICATED");
		}
		return true;
	}

}
