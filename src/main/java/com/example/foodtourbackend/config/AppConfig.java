package com.example.foodtourbackend.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;

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
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> builder
      .simpleDateFormat("yyyy-MM-dd HH:mm:ss")
      .serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
      .deserializers(new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
  }
}