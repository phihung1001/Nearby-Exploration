package com.example.foodtourbackend.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ProviderRequestDTO {
  private final String name;
  private final String address;
  private final String phone;
  private final String email;
  private final String city;
  private final String cityId;
  private final String district;
  private final String districtId;
  private final String photoUrl;
  private final String houseNumber;
}
