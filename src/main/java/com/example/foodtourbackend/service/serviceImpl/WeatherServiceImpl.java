package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.OpenMeteoResponse;
import com.example.foodtourbackend.DTO.HourlyForecastDTO;
import com.example.foodtourbackend.DTO.WeatherDTO;
import com.example.foodtourbackend.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Triển khai dịch vụ thời tiết, lấy dữ liệu thời tiết từ Open-Meteo API
 * và thông tin vị trí từ OpenStreetMap (Nominatim API).
 */
@RequiredArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {

  private final WebClient webClient;
  private final ObjectMapper objectMapper;

  // Bản đồ ánh xạ mã thời tiết sang biểu tượng tương ứng
  private static final Map<Integer, String> WEATHER_ICONS = Map.of(
    0, "☀️", 1, "🌤️", 2, "⛅", 3, "☁️",
    45, "🌫️", 51, "🌦️", 61, "🌧️",
    71, "❄️", 80, "🌦️", 95, "⛈️"
  );

  // Bản đồ mã thời tiết sang mô tả điều kiện thời tiết bằng tiếng Anh
  private static final Map<Integer, String> WEATHER_CONDITIONS_EN = Map.of(
    0, "Clear", 1, "Mainly Clear", 2, "Partly Cloudy", 3, "Overcast"
  );

  // Bản đồ mã thời tiết sang mô tả điều kiện thời tiết bằng tiếng Việt
  private static final Map<Integer, String> WEATHER_CONDITIONS_VI = Map.of(
    0, "Trời quang", 1, "Có nắng nhẹ", 2, "Có mây rải rác", 3, "Trời âm u"
  );

  /**
   * Lấy biểu tượng thời tiết từ mã thời tiết
   * @param code Mã thời tiết
   * @return Biểu tượng tương ứng hoặc mặc định là 🌡️
   */
  private String getWeatherIcon(int code) {
    return WEATHER_ICONS.getOrDefault(code, "🌡️");
  }

  /**
   * Lấy mô tả điều kiện thời tiết bằng tiếng Anh
   * @param code Mã thời tiết
   * @return Mô tả tương ứng hoặc "Unknown" nếu không xác định được
   */
  private String getWeatherConditionEn(int code) {
    return WEATHER_CONDITIONS_EN.getOrDefault(code, "Unknown");
  }

  /**
   * Lấy mô tả điều kiện thời tiết bằng tiếng Việt
   * @param code Mã thời tiết
   * @return Mô tả tương ứng hoặc "Không xác định" nếu không tìm thấy
   */
  private String getWeatherConditionVi(int code) {
    return WEATHER_CONDITIONS_VI.getOrDefault(code, "Không xác định");
  }

  /**
   * Lấy dữ liệu thời tiết và thông tin vị trí từ API
   * @param latitude Vĩ độ
   * @param longitude Kinh độ
   * @param startHour Giờ bắt đầu dự báo
   * @param endHour Giờ kết thúc dự báo
   * @param timezone Múi giờ
   * @return Đối tượng Mono chứa thông tin thời tiết
   */
  public Mono<?> getWeatherData(double latitude, double longitude, String startHour, String endHour, String timezone) {
    String weatherApiUrl = "https://api.open-meteo.com/v1/forecast";
    String geoApiUrl = "https://nominatim.openstreetmap.org/reverse";

    // Gọi API thời tiết
    Mono<String> weatherJsonMono = webClient.mutate().baseUrl(weatherApiUrl).build().get()
      .uri(uriBuilder -> uriBuilder.queryParam("latitude", latitude)
        .queryParam("longitude", longitude)
        .queryParam("hourly", "temperature_2m,weather_code,relative_humidity_2m,uv_index")
        .queryParam("start_hour", startHour)
        .queryParam("end_hour", endHour)
        .queryParam("current", "temperature_2m,weather_code,relative_humidity_2m,uv_index")
        .queryParam("timezone", timezone)
        .build())
      .retrieve().bodyToMono(String.class).onErrorReturn("{}");

    // Gọi API vị trí
    Mono<String> geoJsonMono = webClient.mutate().baseUrl(geoApiUrl).build().get()
      .uri(uriBuilder -> uriBuilder.queryParam("format", "json")
        .queryParam("lat", latitude)
        .queryParam("lon", longitude)
        .queryParam("zoom", 10)
        .build())
      .retrieve().bodyToMono(String.class).onErrorReturn("{}");

    // Xử lý kết quả từ cả hai API
    return Mono.zip(weatherJsonMono, geoJsonMono).map(tuple -> {
      String weatherJson = tuple.getT1();
      String geoJson = tuple.getT2();

      try {
        OpenMeteoResponse response = objectMapper.readValue(weatherJson, OpenMeteoResponse.class);

        // Khởi tạo đối tượng Weather và ánh xạ dữ liệu
        OpenMeteoResponse.CurrentWeather currentWeather = response.getCurrent();
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setTemperature(currentWeather.getTemperature_2m());
        weatherDTO.setCondition(getWeatherConditionEn(currentWeather.getWeather_code()));
        weatherDTO.setConditionText(getWeatherConditionVi(currentWeather.getWeather_code()));
        weatherDTO.setLocation(objectMapper.readTree(geoJson).path("display_name").asText("Unknown"));
        weatherDTO.setHumidity(currentWeather.getRelative_humidity_2m());
        weatherDTO.setUvIndex(currentWeather.getUv_index());

        // Tạo danh sách dự báo theo giờ
        List<HourlyForecastDTO> hourlyForecastDTOS = IntStream.range(0, response.getHourly().getTime().size())
          .mapToObj(i -> new HourlyForecastDTO(
            response.getHourly().getTime().get(i),
            response.getHourly().getTemperature_2m().get(i),
            getWeatherIcon(response.getHourly().getWeather_code().get(i))
          )).collect(Collectors.toList());

        weatherDTO.setForecast(hourlyForecastDTOS);
        return weatherDTO;
      } catch (Exception e) {
        throw new RuntimeException("Lỗi khi phân tích JSON thời tiết", e);
      }
    });
  }
}