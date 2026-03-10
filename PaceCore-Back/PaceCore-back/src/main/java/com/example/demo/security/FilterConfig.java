package com.example.demo.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<FiltroWeb> jwtFilter() {
		FilterRegistrationBean<FiltroWeb> registration = new FilterRegistrationBean<>();
		registration.setFilter(new FiltroWeb());
		registration.addUrlPatterns("/*");
		registration.setOrder(1); // prioridad
		return registration;
	}

}
