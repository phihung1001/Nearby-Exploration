package com.example.foodtourbackend.openai.service.serviceImpl;

import com.example.foodtourbackend.DTO.ExploreRequestDTO;
import com.example.foodtourbackend.DTO.ExploreResponseDTO;
import com.example.foodtourbackend.config.OpenAIConfig;
import com.example.foodtourbackend.mapper.DishesResponseMapper;
import com.example.foodtourbackend.openai.service.OpenAIService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OpenAIServiceImpl implements OpenAIService {
  private static final Logger logger = LoggerFactory.getLogger(OpenAIServiceImpl.class);
  private final RestTemplate restTemplate;
  private final OpenAIConfig config;

  /**
   * Gọi API OpenAI để tạo phản hồi dựa trên yêu cầu của người dùng.
   *
   * @param exploreRequestDTO Dữ liệu đầu vào từ người dùng
   * @return Phản hồi từ OpenAI dưới dạng chuỗi JSON
   */
  public ExploreResponseDTO createChatCompletion(ExploreRequestDTO exploreRequestDTO) {
    String url = config.getBaseUrl() + "/chat/completions";
    String apiKey = config.getApiKey();
    String modelAI = config.getModelAI();

    // Tạo prompt cho AI dựa trên thông tin từ exploreRequestDTO
    String systemPrompt = buildSystemPrompt(exploreRequestDTO);
    String userPrompt = buildUserPrompt(exploreRequestDTO);

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
      System.out.println("answer: " + answer);
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
   * @param exploreRequestDTO Dữ liệu nhập vào từ người dùng
   * @return Chuỗi prompt dành cho người dùng
   */
  private String buildUserPrompt(ExploreRequestDTO exploreRequestDTO) {
    StringBuilder prompt = new StringBuilder();

    // Kiểm tra và thêm thông tin loại bữa ăn
    if (exploreRequestDTO.getMealType() != null && !exploreRequestDTO.getMealType().isEmpty()) {
      prompt.append("Loại bữa ăn: ").append(exploreRequestDTO.getMealType()).append(". ");
    }

    // Kiểm tra và thêm thông tin số người dùng bữa
    if (exploreRequestDTO.getNumberOfPeople() != null) {
      prompt.append("Số người dùng bữa: ").append(exploreRequestDTO.getNumberOfPeople()).append(". ");
    }

    // Kiểm tra và thêm yêu cầu đặc biệt nếu có
    if (exploreRequestDTO.getSpecialRequests() != null && !exploreRequestDTO.getSpecialRequests().isEmpty()) {
      prompt.append("Yêu cầu đặc biệt: ").append(exploreRequestDTO.getSpecialRequests()).append(". ");
    }

    // Kiểm tra và thêm món ăn loại trừ
    if (exploreRequestDTO.getExcludedFoods() != null && !exploreRequestDTO.getExcludedFoods().isEmpty()) {
      prompt.append("Món loại trừ: ").append(String.join(", ", exploreRequestDTO.getExcludedFoods())).append(". ");
    }

    return prompt.toString();
  }

  /**
   * Tạo prompt hệ thống để hướng dẫn AI phản hồi.
   *
   * @param exploreRequestDTO Dữ liệu nhập vào từ người dùng
   * @return Chuỗi prompt dành cho hệ thống
   */
  private String buildSystemPrompt(ExploreRequestDTO exploreRequestDTO) {
    StringBuilder prompt = new StringBuilder("Bạn là chuyên gia ẩm thực tư vấn món ăn cho khách tại ");
    prompt.append(exploreRequestDTO.getLocation()).append(". ");

    // Kiểm tra và thêm thông tin thời tiết nếu có
    if (exploreRequestDTO.getWeather() != null && !exploreRequestDTO.getWeather().isEmpty()) {
      String weatherStr = String.join(", ", exploreRequestDTO.getWeather());
      prompt.append("Hiện tại thời tiết: ").append(weatherStr).append(". ");
    }

    prompt.append("Hãy trả về danh sách 6 món ăn gợi ý phù hợp nhất dưới dạng JSON với format sau: ");
    prompt.append("{ \"title\": \"Tiêu đề\", \"dishes\": [ { \"name\": \"Tên món\", \"description\": \"Mô tả\" } ] }");
    prompt.append(". Chỉ trả về JSON, không giải thích thêm.");
    return prompt.toString();
  }
}
