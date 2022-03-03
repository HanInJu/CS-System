package com.heather.cs.configuration.argumentresolver;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.heather.cs.configuration.annotation.LogInUser;
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
		User user = (User)request.getAttribute(USER_ATTRIBUTE); // 차라리 여기에서 userMapper로 작업을 하는 게 맞음(로직 숨기는 게 되니까)
		// user 어디서 가져온건지 추적을 할 수가 없으니까. 기능을 숨기지 말아야 함
		LogInUser loginUser = parameter.getParameterAnnotation(LogInUser.class);

		if (loginUser == null || user == null) { // loginUser 여기서 왜 또 null 체크하는지? 위에서 이미 체크했는데
			// user null도 Interceptor에서 잘 해줬으면 여기서 null 안해줘도 되잖아.
			// 앞에 interceptor엥서 사용자 유효성을 보장해줬으니까 여기서 꺼내서 써야지 라고 생각해야 한다.
			// request 파이프라인을 머릿속에서 생각해가면서 만들기
			throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다."); // 메시지만 남기지 말고
			// 여기서 exception 메시지가 마땅치않은 경우 exception이 터질 자리가 맞는지 생각해보기
		}

		return user;
	}
}
