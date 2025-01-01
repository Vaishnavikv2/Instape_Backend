package com.instape.app.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {     
        registry.addMapping("/**")
        .allowedOriginPatterns("https://dev-sportal.instape.com", "https://sportal.instape.com") // replace "*" with your specific origin if needed
        .allowedMethods("*")
        .allowedHeaders("*")
        //.allowedOrigins("https://dev-sportal.instape.com", "https://sportal.instape.com")
        .allowCredentials(true);
    }
    
}
