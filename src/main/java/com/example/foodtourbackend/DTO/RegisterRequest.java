package com.example.foodtourbackend.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisterRequest {
    private final String fullName;
    private final String email;
    private final String password;
    private final String confirmPassword;
    private final String phoneNumber;
    private final String address;
    private final String gender;
}
