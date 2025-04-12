package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String address;
    private String gender;
}
