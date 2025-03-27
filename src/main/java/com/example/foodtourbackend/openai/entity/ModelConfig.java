package com.example.foodtourbackend.openai.entity;

public class ModelConfig {
  private String apiKey;
  private String baseURL;
  private String description;
  private int maxConcurrentRequests;

  public ModelConfig() {
  }

  public ModelConfig(String apiKey, String baseURL, String description, int maxConcurrentRequests) {
    this.apiKey = apiKey;
    this.baseURL = baseURL;
    this.description = description;
    this.maxConcurrentRequests = maxConcurrentRequests;
  }
  public String getApiKey() {
    return apiKey;
  }
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }
  public String getBaseURL() {
    return baseURL;
  }
  public void setBaseURL(String baseURL) {
    this.baseURL = baseURL;
  }
  public String getDescription() {
    return description;
  }
}
