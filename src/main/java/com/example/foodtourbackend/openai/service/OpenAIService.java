package com.example.foodtourbackend.openai.service;

import com.example.foodtourbackend.DTO.request.ExploreRequestDTO;
import com.example.foodtourbackend.DTO.response.ExploreResponseDTO;
import jakarta.validation.Valid;

import java.util.Map;

public interface OpenAIService {

  ExploreResponseDTO createChatCompletion(@Valid ExploreRequestDTO request);

  Map<String, String> chatWithAi(Map<String, String> payload);
}