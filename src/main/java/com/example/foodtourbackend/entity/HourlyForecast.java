package com.example.foodtourbackend.entity;

public class HourlyForecast {
  private String time;
  private double temperature;
  private String icon;

  public HourlyForecast() {}
  public HourlyForecast(String time, double temperature, String icon) {
    this.time = time;
    this.temperature = temperature;
    this.icon = icon;
  }
  public String getTime() {
    return time;
  }
  public void setTime(String time) {
    this.time = time;
  }
  public double getTemperature() {
    return temperature;
  }
  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }
  public String getIcon() {
    return icon;
  }
  public void setIcon(String icon) {
    this.icon = icon;
  }

}
