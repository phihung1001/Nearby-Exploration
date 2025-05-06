package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.WeatherDTO;
import com.example.foodtourbackend.DTO.response.OpenMeteoResponseDTO;
import com.example.foodtourbackend.service.serviceImpl.WeatherServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

  @Mock(lenient = true)
  private WebClient webClient;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private WeatherServiceImpl weatherService;

  private WebClient.RequestHeadersUriSpec<?> uriSpec;
  private WebClient.RequestHeadersSpec<?> headersSpec;
  private WebClient.ResponseSpec responseSpec;

  @BeforeEach
  void setupMocks() {
    uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
    headersSpec = mock(WebClient.RequestHeadersSpec.class);
    responseSpec = mock(WebClient.ResponseSpec.class);
  }

  @Test
  void testGetWeatherData_shouldReturnWeatherDTO() throws Exception {
    // === 1. MOCK WebClient gọi weather API ===
    String weatherJson = "{}";
    WebClient client = WebClient.create(); // dùng client thực để tránh lỗi mutate().baseUrl().build()

    // === 2. MOCK dữ liệu JSON trả về ===
    OpenMeteoResponseDTO.Hourly hourly = new OpenMeteoResponseDTO.Hourly(
      List.of("2025-05-06T10:00"), List.of(30.5),
      List.of(1), List.of(70), List.of(5.2)
    );
    OpenMeteoResponseDTO.CurrentWeather current = new OpenMeteoResponseDTO.CurrentWeather(
      "2025-05-06T10:00", 1, 30.5, 1, 70, 5
    );
    OpenMeteoResponseDTO mockResponse = new OpenMeteoResponseDTO();
    mockResponse.setCurrent(current);
    mockResponse.setHourly(hourly);

    // === 3. MOCK ObjectMapper đọc JSON ===
    when(objectMapper.readValue(anyString(), eq(OpenMeteoResponseDTO.class))).thenReturn(mockResponse);

    JsonNode displayNode = mock(JsonNode.class);
    when(displayNode.asText("Unknown")).thenReturn("Hà Nội, Việt Nam");

    JsonNode geoJsonNode = mock(JsonNode.class);
    when(objectMapper.readTree(anyString())).thenReturn(geoJsonNode);
    when(geoJsonNode.path("display_name")).thenReturn(displayNode);

    // === 4. Gọi service ===
    WeatherServiceImpl testedService = new WeatherServiceImpl(client, objectMapper);
    WeatherDTO weather = testedService
      .getWeatherData(21.03, 105.85, "2025-05-06T00:00", "2025-05-06T12:00", "Asia/Bangkok")
      .block(); // blocking để lấy kết quả test

    // === 5. Xác minh kết quả ===
    assertNotNull(weather);
    assertEquals(30.5, weather.getTemperature());
    assertEquals("Mainly Clear", weather.getCondition());
    assertEquals("Có nắng nhẹ", weather.getConditionText());
    assertEquals(70, weather.getHumidity());
    assertEquals(5, weather.getUvIndex());
    assertEquals("Hà Nội, Việt Nam", weather.getLocation());
    assertEquals(1, weather.getForecast().size());
    assertEquals("🌤️", weather.getForecast().get(0).getIcon());
  }
}
