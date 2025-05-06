package com.example.foodtourbackend.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String fullName;
    private String email;
    private String password;
    private Integer age;
    private String confirmPassword;
    private String phoneNumber;
    private String address;
    private String gender;
}
