package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.DishesDTO;
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
   * @param dishesDTO Thông tin món ăn cần thêm (tên, mô tả, ảnh, giá, liên kết nhà hàng...)
   * @return Dữ liệu món ăn sau khi được lưu (hoặc thông báo lỗi)
   */
  @PostMapping("/add")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> addDishes(@RequestBody DishesDTO dishesDTO) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.addDishes(dishesDTO));
  }

  /**
   * API cập nhật thông tin một món ăn theo ID.
   * Chỉ người dùng có quyền PROVIDER mới có thể thực hiện.
   *
   * @param dishesDTO Dữ liệu mới của món ăn cần cập nhật
   * @param id ID của món ăn cần cập nhật
   * @return Dữ liệu món ăn sau khi cập nhật thành công (hoặc thông báo lỗi)
   */
  @PostMapping("update/{id}")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> updateDishes(
    @RequestBody DishesDTO dishesDTO,
    @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.updateDishes(dishesDTO, id));
  }

}
