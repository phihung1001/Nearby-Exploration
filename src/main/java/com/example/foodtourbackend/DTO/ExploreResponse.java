package com.example.foodtourbackend.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExploreResponse {
  public String title;
  @JsonProperty("dishes")
  public List<DishesResponse> dishesResponseList ;

  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  public static class DishesResponse {
    public String name;
    public String description;
  }

}
