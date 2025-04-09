package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

  private final WeatherService weatherService;

  @GetMapping("/get-weather")
  public Mono<?> getWeather(@RequestParam double latitude,
                            @RequestParam double longitude,
                            @RequestParam(required = false) String startHour,
                            @RequestParam(required = false) String endHour,
                            @RequestParam(defaultValue = "Asia/Bangkok") String timezone) {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    if (startHour == null) {
      startHour = LocalDateTime.now().format(formatter);
    }
    if (endHour == null) {
      endHour = LocalDateTime.now().plusHours(6).format(formatter);
    }

    return weatherService.getWeatherData(latitude, longitude, startHour, endHour, timezone);
  }

}
