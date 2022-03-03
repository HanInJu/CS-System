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

	private final UserMapper userMapper; // 역할에 따라서 매퍼 가져와서 작업하는 경우도 있음.

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Cookie cookie = WebUtils.getCookie(request, "userIdCookie");
		if (cookie != null) {
			User user = userMapper.selectActiveUser(cookie.getValue());
			request.setAttribute("AUTH_USER", user); // ? argumentResolver에서 쓰기 위해서 여기서 넣었다는건데
			// 이러면 로직을 숨기는 게 된다...
		}

		return cookie != null;
	}

	// 매니저 권한에 대한 interceptor : 매니저 권한 체크를 위한 편의성 interceptor
}
