package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.*;
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
