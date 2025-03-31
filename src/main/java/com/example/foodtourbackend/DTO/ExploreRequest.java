package com.example.foodtourbackend.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class ExploreRequest {

  private List<String> excludedFoods;
  private String mealType;
  private Integer numberOfPeople;
  private String specialRequests;

  @NotBlank(message = "Địa điểm không được để trống")
  private String location;
  private List<String> weather;

  public ExploreRequest() {
  }

  public ExploreRequest(List<String> excludedFoods, String mealType, Integer numberOfPeople,
                        String specialRequests, String location, List<String> weather) {
    this.excludedFoods = excludedFoods;
    this.mealType = mealType;
    this.numberOfPeople = numberOfPeople;
    this.specialRequests = specialRequests;
    this.location = location;
    this.weather = weather;
  }

  public String getLocation() {
    return location;
  }
  public void setLocation(String location) {
    this.location = location;
  }
  public List<String> getWeather() {
    return weather;
  }
  public void setWeather(List<String> weather) {
    this.weather = weather;
  }

  public Integer getNumberOfPeople() {
    return numberOfPeople;
  }
  public void setNumberOfPeople(Integer numberOfPeople) {
    this.numberOfPeople = numberOfPeople;
  }

  public String getMealType() {
    return mealType;
  }
  public void setMealType(String mealType) {
    this.mealType = mealType;
  }
  public String getSpecialRequests() {
    return specialRequests;
  }
  public void setSpecialRequests(String specialRequests) {
    this.specialRequests = specialRequests;
  }

  public List<String> getExcludedFoods() {
    return excludedFoods;
  }
  public void setExcludedFoods(List<String> excludedFoods) {
    this.excludedFoods = excludedFoods;
  }

}
