package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerResponse {
  private int id;
  private int age;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String address;
  private String gender;
}
