package com.example.foodtourbackend.openai.service;

import com.example.foodtourbackend.openai.entity.ChatRequest;
import jakarta.validation.Valid;

public interface OpenAIService {

  String createChatCompletion(@Valid ChatRequest request);
}