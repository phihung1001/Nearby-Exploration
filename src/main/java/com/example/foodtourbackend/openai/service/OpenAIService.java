package com.example.foodtourbackend.openai.service;

import com.example.foodtourbackend.openai.config.OpenAIConfig;
import com.example.foodtourbackend.openai.entity.ChatRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

@Service
public class OpenAIService {

  private final RestTemplate restTemplate;
  private final OpenAIConfig config;
  private final Semaphore semaphore = new Semaphore(5); // Giới hạn 5 requests đồng thời

  public OpenAIService(RestTemplate restTemplate, OpenAIConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  public String createChatCompletion(ChatRequest chatRequest) throws InterruptedException {
    semaphore.acquire();
    try {
      String url = config.getBaseUrl() + "/chat/completions";
      String apiKey = config.getApiKey();

      var requestBody = Map.of(
        "model", chatRequest.getModel(),
        "messages", List.of(
          Map.of("role", "system", "content", chatRequest.getSystemPrompt()),
          Map.of("role", "user", "content", chatRequest.getUserPrompt())
        ),
        "response_format", chatRequest.isJsonResponse() ? "json_object" : null
      );

      return restTemplate.postForObject(url, requestBody, String.class);
    } finally {
      semaphore.release();
    }
  }
}