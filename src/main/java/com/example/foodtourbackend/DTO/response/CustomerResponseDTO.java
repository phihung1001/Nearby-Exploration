package com.example.foodtourbackend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerResponseDTO {
  private int id;
  private int age;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String address;
  private String gender;
}
