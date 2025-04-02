package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.ExploreResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public class DishesResponseMapper {

  /**
   * Map chuỗi answer từ API sang ExploreResponse DTO.
   *
   * @param answer Chuỗi trả về từ API OpenAI.
   * @return ExploreResponse chứa tiêu đề và danh sách các món ăn (dishes).
   */
  public static ExploreResponse mapAnswerToExploreResponse(String answer) {
    if (answer == null || answer.isEmpty()) {
      return new ExploreResponse("", new ArrayList<>());
    }
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      // Xóa dấu backtick nếu có
      answer = answer.replaceAll("^`+|`+$", "").trim();
      if (answer.startsWith("json")) {
        answer = answer.substring(4).trim(); // Bỏ "json" và khoảng trắng
      }
      return objectMapper.readValue(answer, ExploreResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
      return new ExploreResponse("", new ArrayList<>());
    }
  }
}
