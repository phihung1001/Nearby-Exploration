package com.example.foodtourbackend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UpdatePasswordRequest {
  private String oldPassword;
  private String newPassword;
  private String confirmNewPassword;
}
