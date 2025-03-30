package com.example.foodtourbackend.openai.service.serviceImpl;

import com.example.foodtourbackend.config.OpenAIConfig;
import com.example.foodtourbackend.openai.entity.ChatRequest;
import com.example.foodtourbackend.openai.service.OpenAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.List;
import java.util.Map;


@Service
public class OpenAIServiceImpl implements OpenAIService {
  private static final Logger logger = LoggerFactory.getLogger(OpenAIServiceImpl.class);
  private final RestTemplate restTemplate;
  private final OpenAIConfig config;

  public OpenAIServiceImpl(RestTemplate restTemplate, OpenAIConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }


  public String createChatCompletion(ChatRequest chatRequest) {
    String url = config.getBaseUrl() + "/chat/completions";
    String apiKey = config.getApiKey();
    System.out.println("apiKey: " + apiKey);
    System.out.println("url: " + url);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    var requestBody = Map.of(
      "model", chatRequest.getModel(),
      "messages", List.of(
        Map.of("role", "system", "content", chatRequest.getSystemPrompt()),
        Map.of("role", "user", "content", chatRequest.getUserPrompt())
      ),
      "max_tokens", 1000
    );
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
    try {
      return restTemplate.postForObject(url, entity, String.class);
    } catch (HttpClientErrorException ex) {
      // Lỗi phía client (4xx)
      logger.error("Lỗi phía client: Mã trạng thái {} - Phản hồi: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
      throw ex;
    } catch (HttpServerErrorException ex) {
      // Lỗi phía server (5xx)
      logger.error("Lỗi phía server: Mã trạng thái {} - Phản hồi: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
      throw ex;
    } catch (ResourceAccessException ex) {
      // Lỗi kết nối hoặc timeout
      logger.error("Lỗi truy cập tài nguyên: {}", ex.getMessage());
      throw ex;
    } catch (RestClientException ex) {
      // Bắt các lỗi khác từ RestTemplate
      logger.error("Lỗi khi gọi API: {}", ex.getMessage());
      throw ex;
    }
  }
}