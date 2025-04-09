package com.example.foodtourbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
  private double temperature;
  private String condition;
  private String conditionText;
  private String location;
  private int humidity;
  private int uvIndex;
  // Thêm dự báo thời tiết
  private List<HourlyForecast> forecast;
}
