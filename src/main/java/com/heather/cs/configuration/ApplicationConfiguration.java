package com.heather.cs.configuration;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.heather.cs.configuration.argumentresolver.UserArgumentResolver;
import com.heather.cs.configuration.interceptor.AuthInterceptor;
import com.heather.cs.user.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@Import({DatabaseConfiguration.class})
@PropertySource("classpath:application.properties")
@EnableWebMvc
@ComponentScan({"com.heather.cs.*"})
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {

	private final UserMapper userMapper;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new UserArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor(userMapper))
			.excludePathPatterns("/user")
			.excludePathPatterns("/logIn");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
	}
}

