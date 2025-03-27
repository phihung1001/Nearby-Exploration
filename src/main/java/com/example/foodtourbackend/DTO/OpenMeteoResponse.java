package com.example.foodtourbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

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

  public double getGenerationtime_ms() {
    return generationtime_ms;
  }

  public void setGenerationtime_ms(double generationtime_ms) {
    this.generationtime_ms = generationtime_ms;
  }

  // Getters and Setters
  public double getLongitude() { return longitude; }
  public void setLongitude(double longitude) { this.longitude = longitude; }

  public double getLatitude() { return latitude; }
  public void setLatitude(double latitude) { this.latitude = latitude; }

  public double getElevation() { return elevation; }
  public void setElevation(double elevation) { this.elevation = elevation; }

  public int getUtc_offset_seconds() { return utc_offset_seconds; }
  public void setUtc_offset_seconds(int utc_offset_seconds) { this.utc_offset_seconds = utc_offset_seconds; }

  public String getTimezone() { return timezone; }
  public void setTimezone(String timezone) { this.timezone = timezone; }

  public String getTimezone_abbreviation() { return timezone_abbreviation; }
  public void setTimezone_abbreviation(String timezone_abbreviation) { this.timezone_abbreviation = timezone_abbreviation; }

  public CurrentWeather getCurrent() { return current; }
  public void setCurrent_weather(CurrentWeather current_weather) { this.current = current; }

  public Hourly getHourly() { return hourly; }
  public void setHourly(Hourly hourly) { this.hourly = hourly; }

  public HourlyUnits getHourly_units() { return hourly_units; }
  public void setHourly_units(HourlyUnits hourly_units) { this.hourly_units = hourly_units; }

  // CurrentWeather Class
  public static class CurrentWeather {
    private String time;
    private int interval;
    private double temperature_2m;
    private int weather_code;
    private int relative_humidity_2m;
    private int uv_index;

    // Getters and Setters
    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public double getTemperature_2m() { return temperature_2m; }
    public void setTemperature_2m(double temperature_2m) { this.temperature_2m = temperature_2m; }

    public int getWeather_code() { return weather_code; }
    public void setWeather_code(int weather_code) { this.weather_code = weather_code; }

    public int getRelative_humidity_2m() { return relative_humidity_2m; }
    public void setRelative_humidity_2m(int relative_humidity_2m) { this.relative_humidity_2m = relative_humidity_2m; }

    public int getUv_index() { return uv_index; }
    public void setUv_index(int uv_index) { this.uv_index = uv_index; }
  }

  // Hourly Class
  public static class Hourly {
    private List<String> time;
    private List<Double> temperature_2m;
    private List<Integer> weather_code;
    private List<Integer> relative_humidity_2m;
    private List<Double> uv_index;

    // Getters and Setters
    public List<String> getTime() { return time; }
    public void setTime(List<String> time) { this.time = time; }

    public List<Double> getTemperature_2m() { return temperature_2m; }
    public void setTemperature_2m(List<Double> temperature_2m) { this.temperature_2m = temperature_2m; }

    public List<Integer> getWeather_code() { return weather_code; }
    public void setWeather_code(List<Integer> weather_code) { this.weather_code = weather_code; }

    public List<Integer> getRelative_humidity_2m() { return relative_humidity_2m; }
    public void setRelative_humidity_2m(List<Integer> relative_humidity_2m) { this.relative_humidity_2m = relative_humidity_2m; }

    public List<Double> getUv_index() { return uv_index; }
    public void setUv_index(List<Double> uv_index) { this.uv_index = uv_index; }
  }

  // HourlyUnits Class
  public static class HourlyUnits {
    private String time;
    private String temperature_2m;
    private String weather_code;
    private String relative_humidity_2m;
    private String uv_index;

    // Getters and Setters
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getTemperature_2m() { return temperature_2m; }
    public void setTemperature_2m(String temperature_2m) { this.temperature_2m = temperature_2m; }

    public String getWeather_code() { return weather_code; }
    public void setWeather_code(String weather_code) { this.weather_code = weather_code; }

    public String getRelative_humidity_2m() { return relative_humidity_2m; }
    public void setRelative_humidity_2m(String relative_humidity_2m) { this.relative_humidity_2m = relative_humidity_2m; }

    public String getUv_index() { return uv_index; }
    public void setUv_index(String uv_index) { this.uv_index = uv_index; }
  }
}
