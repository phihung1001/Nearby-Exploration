package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.DishesRequestDTO;
import com.example.foodtourbackend.service.DishesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các thao tác liên quan đến món ăn (dishes) của nhà hàng.
 * Các API trong controller này yêu cầu người dùng có vai trò PROVIDER.
 */
@RestController
@RequestMapping("/public/dishes")
@RequiredArgsConstructor
public class DishesController {
  private final DishesService dishesService;

  /**
   * API thêm mới một món ăn vào hệ thống.
   * Chỉ người dùng có quyền PROVIDER mới có thể thực hiện.
   *
   * @param dishesRequestDTO Thông tin món ăn cần thêm (tên, mô tả, ảnh, giá, liên kết nhà hàng...)
   * @return Dữ liệu món ăn sau khi được lưu (hoặc thông báo lỗi)
   */
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> addDishes(@RequestBody DishesRequestDTO dishesRequestDTO) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.addDishes(dishesRequestDTO));
  }

  /**
   * API cập nhật thông tin một món ăn theo ID.
   * Chỉ người dùng có quyền PROVIDER mới có thể thực hiện.
   *
   * @param dishesRequestDTO Dữ liệu mới của món ăn cần cập nhật
   * @param id ID của món ăn cần cập nhật
   * @return Dữ liệu món ăn sau khi cập nhật thành công (hoặc thông báo lỗi)
   */
  @PostMapping("update/{id}")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> updateDishes(
    @RequestBody DishesRequestDTO dishesRequestDTO,
    @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.updateDishes(dishesRequestDTO, id));
  }

  /**
   * API lấy danh sách tất cả món ăn của một nhà hàng theo ID.
   * Chỉ người dùng có quyền PROVIDER mới có thể thực hiện.
   *
   * @param restaurantId ID của nhà hàng
   * @return Danh sách các món ăn thuộc nhà hàng đó
   */
  @GetMapping("/list/restaurant/{restaurantId}")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> getDishesByRestaurant(@PathVariable Long restaurantId) {
    return ResponseEntity.ok(dishesService.getAllDishesByRestaurantId(restaurantId));
  }

}
