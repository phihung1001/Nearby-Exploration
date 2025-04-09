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


  public static class DishesResponse {
    public String name;
    public String description;
    public DishesResponse() {}

    public DishesResponse(String name, String description) {
      this.name = name;
      this.description = description;
    }
    public String getName() {
      return name;
    }

    public String getDescription() {
      return description;
    }
    public void setDescription(String description) {
      this.description = description;
    }
    public void setName(String name) {
      this.name = name;
    }

  }

}
