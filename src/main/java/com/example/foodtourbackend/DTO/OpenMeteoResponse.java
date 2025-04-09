package com.example.foodtourbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenMeteoResponse {
  private CurrentWeather current;
  private Hourly hourly;
  private HourlyUnits hourly_units;
  private double latitude;
  private double longitude;
  private double elevation;
  private int utc_offset_seconds;
  private String timezone;
  private String timezone_abbreviation;
  private double generationtime_ms;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CurrentWeather {
    private String time;
    private int interval;
    private double temperature_2m;
    private int weather_code;
    private int relative_humidity_2m;
    private int uv_index;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Hourly {
    private List<String> time;
    private List<Double> temperature_2m;
    private List<Integer> weather_code;
    private List<Integer> relative_humidity_2m;
    private List<Double> uv_index;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class HourlyUnits {
    private String time;
    private String temperature_2m;
    private String weather_code;
    private String relative_humidity_2m;
    private String uv_index;
  }
}
