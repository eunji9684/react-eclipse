package com.example.demo.config;
/*
 * 부트의 서버에서 CORS를 허용하는 형식은 상당히 간단합니다.
 * 이 클래스를 bean 으로 등록해서 서버가 스타트됨과 동시에 메모리에 올리면 되고,
 * 설정하는 방식은 WebMvcConfigurer(Interface)를 상속 해서 cors...() 메서드를 오버라이드 하면 됩니다.
 * 이때 이 메서드에는 설정 정보를 등록하는 파라미터가 같이 오는데, 그 파라미터는 객체의 메서드를(주로 setter)를 이용해서
 * 설정만 하면 끝.
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Config 객체를 빈으로 등록하는 Annotation
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
		
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	
			//registry객체 메서드를 이용해서 등록만하면 끝!!!
			registry
			.addMapping("/**")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("GET","POST","PUT","OPTIONS","PATCH","DELETE")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(3600);
	}	
	
}
