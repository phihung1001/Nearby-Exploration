package com.example.foodtourbackend.entity;

import java.util.ArrayList;
import java.util.List;

public class Weather {
  private double temperature;
  private String condition;
  private String conditionText;
  private String location;
  private int humidity;
  private int uvIndex;
  // Thêm dự báo thời tiết
  private List<HourlyForecast> forecast;

  public Weather() {}
  public Weather(double temperature, String condition, String conditionText, String location, int humidity, int uvIndex) {
    this.temperature = temperature;
    this.condition = condition;
    this.conditionText = conditionText;
    this.location = location;
    this.humidity = humidity;
    this.uvIndex = uvIndex;
    this.forecast = new ArrayList<HourlyForecast>();
  }
  public double getTemperature() {
    return temperature;
  }
  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }
  public String getCondition() {
    return condition;
  }
  public void setCondition(String condition) {
    this.condition = condition;
  }
  public String getConditionText() {
    return conditionText;
  }
  public void setConditionText(String conditionText) {
    this.conditionText = conditionText;
  }
  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public int getHumidity() {
    return humidity;
  }
  public void setHumidity(int humidity) {
    this.humidity = humidity;
  }
  public int getUvIndex() {
    return uvIndex;
  }
  public void setUvIndex(int uvIndex) {
    this.uvIndex = uvIndex;
  }
  public void setHourlyForecasts(List<HourlyForecast> hourlyForecasts) {
    forecast = hourlyForecasts;
  }
  public List<HourlyForecast> getHourlyForecasts() {
    return forecast;
  }
}
