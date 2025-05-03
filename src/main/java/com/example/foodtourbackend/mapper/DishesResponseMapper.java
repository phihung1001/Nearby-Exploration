package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.response.ExploreResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class DishesResponseMapper {

  /**
   * Map chuỗi answer từ API sang ExploreResponseDTO DTO.
   *
   * @param answer Chuỗi trả về từ API OpenAI.
   * @return ExploreResponseDTO chứa tiêu đề và danh sách các món ăn (dishes).
   */
  public static ExploreResponseDTO mapAnswerToExploreResponse(String answer) {
    if (answer == null || answer.isEmpty()) {
      return new ExploreResponseDTO("", new ArrayList<>());
    }
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      // Xóa dấu backtick nếu có
      answer = answer.replaceAll("^`+|`+$", "").trim();
      if (answer.startsWith("json")) {
        answer = answer.substring(4).trim(); // Bỏ "json" và khoảng trắng
      }
      return objectMapper.readValue(answer, ExploreResponseDTO.class);
    } catch (Exception e) {
      e.printStackTrace();
      return new ExploreResponseDTO("", new ArrayList<>());
    }
  }
}
