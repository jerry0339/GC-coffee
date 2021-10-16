package com.example.gccoffee.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Note : WebMvcConfigurer
//  - 원하는 MVC설정을 확장가능

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override // CORS 설정
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("*");
    }
}
