package com.example.foodtourbackend.openai.controller;

import com.example.foodtourbackend.DTO.ExploreRequest;
import com.example.foodtourbackend.openai.service.OpenAIService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/openai")
@RequiredArgsConstructor
public class OpenAIController {

  private final OpenAIService openAIService;
  @PostMapping("/explore")
  public ResponseEntity<?> createChatCompletion(@Valid @RequestBody ExploreRequest request) {
      return ResponseEntity.ok(openAIService.createChatCompletion(request));
  }
}