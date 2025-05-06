package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.request.LoginRequestDTO;
import com.example.foodtourbackend.DTO.request.RegisterRequestDTO;
import com.example.foodtourbackend.DTO.request.TokenRequestDTO;
import com.example.foodtourbackend.DTO.response.LoginResponseDTO;
import com.example.foodtourbackend.DTO.response.TokenResponseDTO;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.mapper.CustomerMapper;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.service.AuthService;
import com.example.foodtourbackend.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;

  @Override
  public  ResponseEntity<?> register(RegisterRequestDTO registerRequestDTO) {
      if (customerRepository.existsByEmail(registerRequestDTO.getEmail())) {
          throw new DuplicateException("Email đã được đăng ký");
      }
      if(customerRepository.existsByPhoneNumber(registerRequestDTO.getPhoneNumber())) {
          throw new DuplicateException("Số điện thoại đã được đăng ký");
      }
      Customer customer = customerMapper.RegisterRequestDTO2Entity(registerRequestDTO);
      customer.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
      customer.setRole("CUSTOMER");
      customerRepository.save(customer);
      return ResponseEntity.ok(Map.of("message","Đăng kí thành công"));
  }
  @Override
  public ResponseEntity<?> login(LoginRequestDTO request, HttpServletResponse response) {
      Customer customer = customerRepository.findByEmail(request.getEmail())
              .orElseThrow(() -> new NotFoundException("Email không tồn tại"));

      if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
          throw new UnauthorizedException("Mật khẩu không chính xác");
      }
      String accessToken = jwtUtil.generateToken(customer);
      String refreshToken = jwtUtil.generateRefreshToken(customer);
      // Tạo cookie chứa refresh token
      Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
      refreshCookie.setHttpOnly(true);           // Không cho phép truy cập từ JavaScript
      refreshCookie.setSecure(false);
      refreshCookie.setPath("/");                  // Cookie được gửi với mọi request
      refreshCookie.setMaxAge(7 * 24 * 60 * 60);
      response.addCookie(refreshCookie);

      LoginResponseDTO tokenRespone = new LoginResponseDTO(accessToken);
      return ResponseEntity.ok(tokenRespone);
  }

  @Override
  public ResponseEntity<?> checkUser(TokenRequestDTO token) {
      return ResponseEntity.ok(jwtUtil.getAllClaimsFromToken(token.getToken()));
  }

  public TokenResponseDTO refreshToken(HttpServletRequest request) {
      String refreshToken = null;
      Cookie[] cookies = request.getCookies();
      if (cookies != null) {
          for (Cookie cookie : cookies) {
              if ("refreshToken".equals(cookie.getName())) {
                  refreshToken = cookie.getValue();
                  break;
              }
          }
      }
      if (refreshToken == null) {
          throw new UnauthorizedException("Refresh token không hợp lệ hoặc đã hết hạn");
      }

      String email = jwtUtil.getUserEmailFromToken(refreshToken);
      UserDetails userDetails = userDetailsService.loadUserByUsername(email);
      if (!jwtUtil.validateToken(refreshToken, userDetails)) {
          throw new UnauthorizedException("Refresh token không hợp lệ hoặc đã hết hạn");
      }
      Customer customer = customerRepository.findByEmail(email)
              .orElseThrow(() -> new NotFoundException("Customer không tồn tại"));
      String newAccessToken = jwtUtil.generateToken(customer);
      return new TokenResponseDTO(newAccessToken);
  }
}