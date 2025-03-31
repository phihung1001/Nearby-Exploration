package com.example.foodtourbackend.openai.service.serviceImpl;

import com.example.foodtourbackend.DTO.ExploreRequest;
import com.example.foodtourbackend.DTO.ExploreResponse;
import com.example.foodtourbackend.config.OpenAIConfig;
import com.example.foodtourbackend.mapper.DishesResponseMapper;
import com.example.foodtourbackend.openai.service.OpenAIService;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OpenAIServiceImpl implements OpenAIService {
  private static final Logger logger = LoggerFactory.getLogger(OpenAIServiceImpl.class);
  private final RestTemplate restTemplate;
  private final OpenAIConfig config;


  public OpenAIServiceImpl(RestTemplate restTemplate, OpenAIConfig config) {
    this.restTemplate = restTemplate;
    this.config = config;
  }

  /**
   * Gọi API OpenAI để tạo phản hồi dựa trên yêu cầu của người dùng.
   *
   * @param exploreRequest Dữ liệu đầu vào từ người dùng
   * @return Phản hồi từ OpenAI dưới dạng chuỗi JSON
   */
  public ExploreResponse createChatCompletion(ExploreRequest exploreRequest) {
    String url = config.getBaseUrl() + "/chat/completions";
    String apiKey = config.getApiKey();
    String modelAI = config.getModelAI();

    // Tạo prompt cho AI dựa trên thông tin từ exploreRequest
    String systemPrompt = buildSystemPrompt(exploreRequest);
    String userPrompt = buildUserPrompt(exploreRequest);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(apiKey);

    var requestBody = Map.of(
      "model", modelAI,
      "messages", List.of(
        Map.of("role", "system", "content", systemPrompt),
        Map.of("role", "user", "content", userPrompt)
      ),
      "max_tokens", 1000,
      "jsonResponse", true
    );

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    try {
      JsonNode responseJson = restTemplate.postForObject(url, entity, JsonNode.class);
      String answer = responseJson.path("choices").get(0).path("message").path("content").asText();
      return DishesResponseMapper.mapAnswerToExploreResponse(answer);

    } catch (HttpClientErrorException ex) {
      // Xử lý lỗi phía client (4xx)
      logger.error("Lỗi phía client: Mã trạng thái {} - Phản hồi: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
      throw ex;
    } catch (HttpServerErrorException ex) {
      // Xử lý lỗi phía server (5xx)
      logger.error("Lỗi phía server: Mã trạng thái {} - Phản hồi: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
      throw ex;
    } catch (ResourceAccessException ex) {
      // Xử lý lỗi kết nối hoặc timeout
      logger.error("Lỗi truy cập tài nguyên: {}", ex.getMessage());
      throw ex;
    } catch (RestClientException ex) {
      // Bắt các lỗi khác từ RestTemplate
      logger.error("Lỗi khi gọi API: {}", ex.getMessage());
      throw ex;
    }
  }

  /**
   * Tạo prompt cho người dùng dựa trên yêu cầu nhập vào.
   *
   * @param exploreRequest Dữ liệu nhập vào từ người dùng
   * @return Chuỗi prompt dành cho người dùng
   */
  private String buildUserPrompt(ExploreRequest exploreRequest) {
    StringBuilder prompt = new StringBuilder();

    // Kiểm tra và thêm thông tin loại bữa ăn
    if (exploreRequest.getMealType() != null && !exploreRequest.getMealType().isEmpty()) {
      prompt.append("Loại bữa ăn: ").append(exploreRequest.getMealType()).append(". ");
    }

    // Kiểm tra và thêm thông tin số người dùng bữa
    if (exploreRequest.getNumberOfPeople() != null) {
      prompt.append("Số người dùng bữa: ").append(exploreRequest.getNumberOfPeople()).append(". ");
    }

    // Kiểm tra và thêm yêu cầu đặc biệt nếu có
    if (exploreRequest.getSpecialRequests() != null && !exploreRequest.getSpecialRequests().isEmpty()) {
      prompt.append("Yêu cầu đặc biệt: ").append(exploreRequest.getSpecialRequests()).append(". ");
    }

    // Kiểm tra và thêm món ăn loại trừ
    if (exploreRequest.getExcludedFoods() != null && !exploreRequest.getExcludedFoods().isEmpty()) {
      prompt.append("Món loại trừ: ").append(String.join(", ", exploreRequest.getExcludedFoods())).append(". ");
    }

    return prompt.toString();
  }

  /**
   * Tạo prompt hệ thống để hướng dẫn AI phản hồi.
   *
   * @param exploreRequest Dữ liệu nhập vào từ người dùng
   * @return Chuỗi prompt dành cho hệ thống
   */
  private String buildSystemPrompt(ExploreRequest exploreRequest) {
    StringBuilder prompt = new StringBuilder("Bạn là chuyên gia ẩm thực tư vấn món ăn cho khách tại ");
    prompt.append(exploreRequest.getLocation()).append(". ");

    // Kiểm tra và thêm thông tin thời tiết nếu có
    if (exploreRequest.getWeather() != null && !exploreRequest.getWeather().isEmpty()) {
      String weatherStr = String.join(", ", exploreRequest.getWeather());
      prompt.append("Hiện tại thời tiết: ").append(weatherStr).append(". ");
    }

    prompt.append("Hãy đưa ra gợi ý món ăn phù hợp.");
    return prompt.toString();
  }

  public List<String> extractDishNames(String content) {
    List<String> dishNames = new ArrayList<>();
    // Biểu thức regex tìm chuỗi nằm giữa ** và ** sau dấu số và dấu chấm
    Pattern pattern = Pattern.compile("\\d+\\. \\*\\*(.*?)\\*\\*:");
    Matcher matcher = pattern.matcher(content);
    while (matcher.find()) {
      dishNames.add(matcher.group(1).trim());
    }
    return dishNames;
  }
}
