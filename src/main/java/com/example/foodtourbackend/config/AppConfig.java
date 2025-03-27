package com.example.foodtourbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  @Bean
  public WebClient defaultClient(WebClient.Builder builder) {
    return builder.build();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}