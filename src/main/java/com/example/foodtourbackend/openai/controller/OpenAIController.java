package com.example.foodtourbackend.openai.controller;

import com.example.foodtourbackend.openai.entity.ChatRequest;
import com.example.foodtourbackend.openai.service.OpenAIService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

  private final OpenAIService openAIService;

  public OpenAIController(OpenAIService openAIService) {
    this.openAIService = openAIService;
  }

  @PostMapping("/explore")
  public ResponseEntity<String> createChatCompletion(@Valid @RequestBody ChatRequest request) throws InterruptedException {
      return ResponseEntity.ok(openAIService.createChatCompletion(request));
  }
}