package com.heather.cs.configuration;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.heather.cs.configuration.argumentresolver.UserArgumentResolver;
import com.heather.cs.configuration.interceptor.AuthenticationInterceptor;
import com.heather.cs.configuration.interceptor.ManagerPrivilegeInterceptor;
import com.heather.cs.converter.DateConverter;
import com.heather.cs.converter.DateFormatter;

import lombok.RequiredArgsConstructor;

@Configuration
@Import({DatabaseConfiguration.class})
@PropertySource("classpath:application.properties")
@EnableWebMvc
@ComponentScan({"com.heather.cs.*"})
@RequiredArgsConstructor
public class ApplicationConfiguration implements WebMvcConfigurer {

	private final AuthenticationInterceptor authenticationInterceptor;
	private final ManagerPrivilegeInterceptor managerPrivilegeInterceptor;
	private final UserArgumentResolver userArgumentResolver;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.excludePathPatterns("/")
			.excludePathPatterns("/user")
			.excludePathPatterns("/logIn");
		registry.addInterceptor(managerPrivilegeInterceptor)
			.addPathPatterns("/counsels/**")
			.addPathPatterns("/statistics/**");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DateConverter());
		// registry.addFormatter(new DateFormatter());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new MappingJackson2HttpMessageConverter());
	}
}

