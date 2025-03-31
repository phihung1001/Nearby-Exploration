package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.ExploreResponse;
import com.example.foodtourbackend.DTO.ExploreResponse.DishesResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // Tìm phần tiêu đề: Văn bản trước lần xuất hiện đầu tiên của "**"
    int firstDishIndex = answer.indexOf("**");
    String title = (firstDishIndex != -1) ? answer.substring(0, firstDishIndex).trim() : answer;
    title = title.replaceAll("\\s*\\n\\s*\\d+\\.?\\s*$", "").trim();


    // Phần chứa danh sách các dish, bắt đầu từ lần xuất hiện đầu tiên của "**"
    String dishesPart = (firstDishIndex != -1) ? answer.substring(firstDishIndex) : "";

    List<DishesResponse> dishesResponseList = new ArrayList<>();

    // Biểu thức regex để trích xuất các dish:
    // Tìm chuỗi bắt đầu bằng "**", sau đó tên dish nằm giữa "**" và ":".
    // Sau dấu ":" là phần mô tả (bao gồm cả dòng mới, dấu '-' bullet, ...),
    // cho đến khi gặp "**" mới hoặc kết thúc chuỗi.
    Pattern pattern = Pattern.compile("\\*\\*(.+?)\\*\\*:\\s*([\\s\\S]+?)(?=\\*\\*|$)");
    Matcher matcher = pattern.matcher(dishesPart);

    while (matcher.find()) {
      String dishName = matcher.group(1).trim();
      String description = matcher.group(2).trim();
      description = description.replaceAll("\\s*\\n\\s*\\d+\\.?\\s*$", "").trim();

      DishesResponse dish = new DishesResponse(dishName, description);
      dishesResponseList.add(dish);
    }

    return new ExploreResponse(title, dishesResponseList);
  }
}
