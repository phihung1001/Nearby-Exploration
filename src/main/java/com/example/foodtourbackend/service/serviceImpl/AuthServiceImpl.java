package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.*;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.Customer;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    public  ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (customerRepository.existsByEmail(registerRequest.getEmail())) {
            throw new DuplicateException("Email đã được đăng ký");
        }
        if(customerRepository.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new DuplicateException("Số điện thoại đã được đăng ký");
        }
        Customer customer = new Customer();
        customer.setFullName(registerRequest.getFullName());
        customer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        customer.setEmail(registerRequest.getEmail());
        customer.setPhoneNumber(registerRequest.getPhoneNumber());
        customer.setAddress(registerRequest.getAddress());
        customer.setGender(registerRequest.getGender());
        customer.setRole("CUSTOMER");
        customerRepository.save(customer);
        return ResponseEntity.ok(Map.of("message","Đăng kí thành công"));
    }

    @Override
    public ResponseEntity<?> login(LoginRequest request,HttpServletResponse response) {
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

        LoginResponse tokenRespone = new LoginResponse(accessToken);
        return ResponseEntity.ok(tokenRespone);
    }

    @Override
    public ResponseEntity<?> checkUser(TokenRequest token) {
        return ResponseEntity.ok(jwtUtil.getAllClaimsFromToken(token.getToken()));
    }

    public TokenResponse refreshToken(HttpServletRequest request) {
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
        return new TokenResponse(newAccessToken);
    }
}
