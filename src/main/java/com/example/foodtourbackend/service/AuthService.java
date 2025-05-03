package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.LoginRequestDTO;
import com.example.foodtourbackend.DTO.request.RegisterRequestDTO;
import com.example.foodtourbackend.DTO.request.TokenRequestDTO;
import com.example.foodtourbackend.DTO.response.TokenResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<?> register(RegisterRequestDTO request);

    ResponseEntity<?> login(LoginRequestDTO request, HttpServletResponse response);
    ResponseEntity<?> checkUser(TokenRequestDTO token);
    TokenResponseDTO refreshToken(HttpServletRequest request);

}
