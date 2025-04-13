package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProviderResponseDTO {
  private Long id;
  private String name;
  private String phone;
  private String email;
  private String address;
  private String city;
  private String district;
  private String photoUrl;
  private String houseNumber;
}
