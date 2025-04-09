package com.example.foodtourbackend.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExploreRequest {

  private List<String> excludedFoods;
  private String mealType;
  private Integer numberOfPeople;
  private String specialRequests;

  @NotBlank(message = "Địa điểm không được để trống")
  private String location;
  private List<String> weather;

}
