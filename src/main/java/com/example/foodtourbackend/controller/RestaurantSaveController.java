package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.service.RestaurantSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customer/restaurant-save")
public class RestaurantSaveController {
  private final RestaurantSaveService restaurantSaveService;

  /**
   * API lấy danh sách nhà hàng đã lưu có phân trang
   *
   * @param page Trang hiện tại (mặc định là 0)
   * @param size Số lượng nhà hàng mỗi trang (mặc định là 20)
   * @return Danh sách nhà hàng theo phân trang
   */
  @GetMapping("/list")
  public ResponseEntity<Page<?>> getRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(restaurantSaveService.getRestaurants(page, size));
  }
}
