package com.example.foodtourbackend.openai.service;

import com.example.foodtourbackend.DTO.ExploreRequestDTO;
import com.example.foodtourbackend.DTO.ExploreResponseDTO;
import jakarta.validation.Valid;

public interface OpenAIService {

  ExploreResponseDTO createChatCompletion(@Valid ExploreRequestDTO request);
}