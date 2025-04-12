package com.example.foodtourbackend.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequestDTO {
  private String oldPassword;
  private String newPassword;
  private String confirmNewPassword;
}
