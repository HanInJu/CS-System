package com.heather.cs.argumentresolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.heather.cs.annotation.LogInUser;
import com.heather.cs.user.dto.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

	public final String USER_ATTRIBUTE = "AUTH_USER";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(LogInUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
		User user = (User)request.getAttribute(USER_ATTRIBUTE);
		LogInUser loginUser = parameter.getParameterAnnotation(LogInUser.class);

		if (loginUser == null || user == null) {
			throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다.");
		}

		return user;
	}
}
