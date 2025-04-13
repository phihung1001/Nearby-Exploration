package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishesResponseDTO {
  private String name;
  private String description;
  private String image;
  private String price;
  private Long restaurantId;
}
