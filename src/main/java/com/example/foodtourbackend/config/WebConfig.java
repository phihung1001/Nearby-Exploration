package com.example.foodtourbackend.config;  // Điều chỉnh package theo project của em

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Cho phép tất cả URL
                        .allowedOrigins("http://localhost:3000")  // Cho phép React gọi
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Các method được phép
                        .allowedHeaders("*")  // Cho phép mọi header
                        .allowCredentials(true);  // Cho phép gửi cookies nếu cần
            }
        };
    }
}
