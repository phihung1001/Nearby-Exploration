package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.request.LoginRequestDTO;
import com.example.foodtourbackend.DTO.request.RegisterRequestDTO;
import com.example.foodtourbackend.DTO.request.TokenRequestDTO;
import com.example.foodtourbackend.DTO.response.TokenResponseDTO;
import com.example.foodtourbackend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller chịu trách nhiệm xử lý các yêu cầu liên quan đến xác thực (authentication)
 * và đăng ký tài khoản (registration).
 *
 * Bao gồm:
 * 1. Đăng ký tài khoản mới (signup)
 * 2. Đăng nhập (signin)
 * 3. Kiểm tra token (getUserToken)
 * 4. Tạo mới token (refresh)
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;
  /**
   * Đăng ký tài khoản (Sign Up).
   *
   * @param registerRequestDTO Dữ liệu đăng ký tài khoản (tên, email, mật khẩu, v.v.)
   * @return Trả về thông tin tài khoản vừa được đăng ký, hoặc lỗi nếu không hợp lệ.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
    // Gọi service để thực hiện đăng ký
    // Trả về HTTP 200 (OK) nếu thành công, kèm dữ liệu tài khoản mới
    return ResponseEntity.status(HttpStatus.OK).body(authService.register(registerRequestDTO));
  }

  /**
   * Đăng nhập (Sign In).
   *
   * @param loginRequestDTO Thông tin đăng nhập (email, password)
   * @param response     HttpServletResponse, dùng để set cookie refresh token nếu cần
   * @return Trả về token (JWT) hoặc thông báo lỗi nếu thông tin không hợp lệ.
   */
  @PostMapping("/signin")
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
    // Gọi service để xác thực và trả về JWT
    return authService.login(loginRequestDTO, response);
  }

  /**
   * Kiểm tra token người dùng (getUserToken).
   *
   * @param token TokenRequestDTO chứa token cần kiểm tra
   * @return Thông tin người dùng tương ứng với token hoặc lỗi nếu token không hợp lệ
   */
  @PostMapping("/getUserToken")
  public ResponseEntity<?> checkUser(@RequestBody TokenRequestDTO token) {
    // Gọi service để kiểm tra token
    return authService.checkUser(token);
  }

  /**
   * Refresh token - tạo mới Access Token dựa trên Refresh Token.
   *
   * @param request HttpServletRequest, có thể chứa refresh token trong cookie
   * @return TokenResponseDTO chứa Access Token mới hoặc lỗi nếu refresh token không hợp lệ
   */
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponseDTO> refreshToken(HttpServletRequest request) {
    // Gọi service để sinh Access Token mới
    TokenResponseDTO tokenResponseDTO = authService.refreshToken(request);
    return ResponseEntity.ok(tokenResponseDTO);
  }
}
