package com.example.foodtourbackend.service;

import reactor.core.publisher.Mono;

public interface WeatherService {

  Mono<?> getWeatherData(double latitude, double longitude, String startHour, String endHour, String timezone);
}
