package com.example.foodtourbackend.openai.entity;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {
  @NotBlank
  private String model;

  @NotBlank
  private String systemPrompt;

  @NotBlank
  private String userPrompt;

  private boolean jsonResponse = true;


  public ChatRequest(String model, String systemPrompt, String userPrompt) {
    this.model = model;
    this.systemPrompt = systemPrompt;
    this.userPrompt = userPrompt;
  }

  public boolean isJsonResponse() {
    return jsonResponse;
  }

  public void setJsonResponse(boolean jsonResponse) {
    this.jsonResponse = jsonResponse;
  }
  public String getModel() {
    return model;
  }
  public void setModel(String model) {
    this.model = model;
  }
  public String getSystemPrompt() {
    return systemPrompt;
  }
  public void setSystemPrompt(String systemPrompt) {
    this.systemPrompt = systemPrompt;
  }
  public String getUserPrompt() {
    return userPrompt;
  }
  public void setUserPrompt(String userPrompt) {
    this.userPrompt = userPrompt;
  }

}
