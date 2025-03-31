package com.example.foodtourbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfig {
  @Value("${openai.api.key}")
  private String apiKey;

  @Value("${openai.base.url}")
  private String baseURL;

  @Value("${model.ai}")
  private String modelAI;

  public String getModelAI() {
    return modelAI;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getBaseUrl() {
    return baseURL;
  }
}