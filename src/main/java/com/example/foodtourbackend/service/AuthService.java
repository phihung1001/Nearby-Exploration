package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.LoginRequest;
import com.example.foodtourbackend.DTO.RegisterRequest;
import com.example.foodtourbackend.DTO.TokenRequest;
import com.example.foodtourbackend.DTO.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<?> register(RegisterRequest request);

    ResponseEntity<?> login(LoginRequest request, HttpServletResponse response);
    ResponseEntity<?> checkUser(TokenRequest token);
    TokenResponse refreshToken(HttpServletRequest request);
}
