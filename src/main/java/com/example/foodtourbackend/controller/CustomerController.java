package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.request.CommentDTO;
import com.example.foodtourbackend.DTO.request.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.request.UpdatePasswordRequestDTO;
import com.example.foodtourbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller dành cho các thao tác liên quan đến khách hàng (Customer).
 *
 * Gồm các chức năng:
 * 1. Lấy thông tin khách hàng theo ID
 * 2. Cập nhật thông tin khách hàng
 * 3. Đổi mật khẩu khách hàng
 * 4. Tìm kiếm dựa trên ảnh (demo)
 */
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

  private final CustomerService customerService;

  /**
   * Lấy thông tin khách hàng theo ID
   *
   * @param id ID của khách hàng cần lấy
   * @return Thông tin chi tiết của khách hàng (CustomerResponseDTO )
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    // Gọi service để lấy thông tin khách hàng
    // Trả về HTTP 200 (OK) kèm dữ liệu nếu thành công
    return ResponseEntity.status(HttpStatus.OK).body(customerService.getById(id));
  }

  /**
   * Cập nhật thông tin khách hàng
   *
   * @param customerRequestDTO Thông tin cần cập nhật (JSON trong body)
   * @param id          ID của khách hàng cần cập nhật
   * @return Thông tin khách hàng đã được cập nhật hoặc thông báo lỗi
   */
  @PostMapping("/update/{id}")
  public ResponseEntity<?> update(
    @RequestBody CustomerRequestDTO customerRequestDTO,
    @PathVariable Long id
  ) {
    // Gọi service để cập nhật thông tin khách hàng
    // Trả về HTTP 200 (OK) kèm dữ liệu đã cập nhật nếu thành công
    return ResponseEntity.status(HttpStatus.OK).body(customerService.update(id, customerRequestDTO));
  }

  /**
   * Cập nhật mật khẩu khách hàng
   *
   * @param request Dữ liệu chứa mật khẩu cũ, mật khẩu mới
   * @param id      ID của khách hàng cần cập nhật mật khẩu
   * @return Thông báo thành công hoặc lỗi
   */
  @PostMapping("/update/password/{id}")
  public ResponseEntity<?> updatePassword(
    @RequestBody UpdatePasswordRequestDTO request,
    @PathVariable Long id
  ) {
    // Gọi service để cập nhật mật khẩu
    // Trả về HTTP 200 (OK) kèm thông báo JSON nếu thành công
    return ResponseEntity.status(HttpStatus.OK).body(customerService.updatePassword(id, request));
  }

  /**
   * Tìm kiếm thông tin dựa trên ảnh
   *
   * @param file Ảnh được upload dưới dạng MultipartFile
   * @return Chuỗi thông báo hoặc kết quả tìm kiếm
   */
  @PostMapping("/searchByImage")
  public ResponseEntity<String> searchByImage(@RequestParam("file") MultipartFile file) {
    // Kiểm tra file ảnh hợp lệ
    if (file == null || file.isEmpty()) {
      // Nếu không hợp lệ, trả về 400 Bad Request
      return ResponseEntity.badRequest().body("File ảnh không hợp lệ");
    }
    // Gọi service để xử lý ảnh và trả về kết quả
    return ResponseEntity.ok(customerService.searchByImage(file));
  }

  /**
   * Nâng quyền người dùng
   *
   * @return thành công hoặc lỗi
   */
  @PostMapping("/upgrade-provider")
  public ResponseEntity<?> upgrade() {
    return ResponseEntity.ok(customerService.upgrade());
  }

  /**
   * Comment nhà hàng
   *
   * commentDTO là body request
   * @return thành công hoặc lỗi
   */
  @PostMapping("/comment")
  public ResponseEntity<?> comment(
    @RequestBody CommentDTO commentDTO) {
    return ResponseEntity.ok(customerService.comment(commentDTO));
  }

}
