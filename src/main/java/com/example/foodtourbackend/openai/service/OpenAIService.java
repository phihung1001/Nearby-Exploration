package com.example.foodtourbackend.openai.service;

import com.example.foodtourbackend.DTO.ExploreRequest;
import com.example.foodtourbackend.DTO.ExploreResponse;
import jakarta.validation.Valid;

public interface OpenAIService {

  ExploreResponse createChatCompletion(@Valid ExploreRequest request);
}