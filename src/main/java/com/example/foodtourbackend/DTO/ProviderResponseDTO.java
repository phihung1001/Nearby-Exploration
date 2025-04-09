package com.example.foodtourbackend.DTO;

import com.example.foodtourbackend.entity.CategoryFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProviderResponseDTO {
  private String name;
  private String address;
  private Integer cityId;
  private Integer districtId;
  private String photoUrl;
  private String houseNumber;
  private List<CategoryFood> dishes;
}
