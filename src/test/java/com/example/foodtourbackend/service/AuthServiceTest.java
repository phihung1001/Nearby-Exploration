package com.example.foodtourbackend.service;

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
import com.example.foodtourbackend.service.serviceImpl.AuthServiceImpl;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import com.example.foodtourbackend.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private UserDetailsService userDetailsService;
  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private AuthServiceImpl authService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private CustomerMapper customerMapper;

  private MockHttpServletResponse mockResponse;

  @BeforeEach
  void setupAuthentication() {
    UserDetailsImpl userDetails = new UserDetailsImpl(1L,
      "testuser",
      "password",
      List.of(new SimpleGrantedAuthority("CUSTOMER")));

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      userDetails, null, userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Test
  void testLogin_Success() {
    SecurityContextHolder.clearContext();
    // Arrange
    String email = "testuser@example.com";
    String password = "password";
    LoginRequestDTO request = new LoginRequestDTO(email, password);

    Customer customer = new Customer();
    customer.setEmail(email);
    customer.setPassword("$2a$10$K1l1m8S7hFgHtGH4zTKv7hLD13tWnEpsZsh5vF1gxwTP3T1JcAmG"); // Mật khẩu đã được mã hóa

    when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
    when(jwtUtil.generateToken(customer)).thenReturn("access_token");
    when(jwtUtil.generateRefreshToken(customer)).thenReturn("refresh_token");

    HttpServletResponse mockResponse = mock(HttpServletResponse.class);
    // Mock hành vi của addCookie
    doNothing().when(mockResponse).addCookie(any(Cookie.class));

    // Act
    ResponseEntity<?> responseEntity = authService.login(request, mockResponse);

    // Assert
    assertEquals(200, responseEntity.getStatusCodeValue());
    LoginResponseDTO loginResponseDTO = (LoginResponseDTO) responseEntity.getBody();
    assertNotNull(loginResponseDTO);
    assertEquals("access_token", loginResponseDTO.getAccessToken());
    verify(customerRepository).findByEmail(email);
    verify(mockResponse).addCookie(any(Cookie.class));
  }

  @Test
  void testLogin_EmailNotFound() {
    // Arrange
    String email = "notfound@example.com";
    String password = "password";
    LoginRequestDTO request = new LoginRequestDTO(email, password);

    when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      authService.login(request, mockResponse);
    });
    assertEquals("Email không tồn tại", exception.getMessage());
  }

  @Test
  void testLogin_InvalidPassword() {
    LoginRequestDTO request = new LoginRequestDTO("testuser@example.com", "wrongpassword");

    Customer customer = new Customer();
    customer.setEmail("testuser@example.com");
    customer.setPassword("$2a$10$K1l1m8S7hFgHtGH4zTKv7hLD13tW.nEpsZsh5vF1gxwTP3T1JcAmG");

    when(customerRepository.findByEmail("testuser@example.com")).thenReturn(Optional.of(customer));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      authService.login(request, mockResponse);
    });
    assertEquals("Mật khẩu không chính xác", exception.getMessage());  // Kiểm tra thông báo lỗi
  }

  @Test
  void testRegister_Success() {
    // Arrange
    RegisterRequestDTO request = new RegisterRequestDTO();
    request.setFullName("Nguyen Van A");
    request.setEmail("a@example.com");
    request.setPassword("123456");
    request.setPhoneNumber("0123456789");
    request.setAddress("Hanoi");
    request.setGender("MALE");

    Customer customer = new Customer();
    customer.setFullName("Nguyen Van A");
    customer.setEmail("a@example.com");
    customer.setPassword("123456");
    customer.setPhoneNumber("0123456789");
    customer.setAddress("Hanoi");
    customer.setGender("MALE");

    when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
    when(customerRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(false);
    when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded_password");
    when(customerMapper.RegisterRequestDTO2Entity(request)).thenReturn(customer);

    // Act
    ResponseEntity<?> response = authService.register(request);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertTrue(response.getBody() instanceof Map);
    assertEquals("Đăng kí thành công", ((Map<?, ?>) response.getBody()).get("message"));

    verify(customerRepository).save(any(Customer.class));
  }

  @Test
  void testRegister_EmailAlreadyExists() {
    // Arrange
    RegisterRequestDTO request = new RegisterRequestDTO();
    request.setEmail("a@example.com");

    when(customerRepository.existsByEmail(request.getEmail())).thenReturn(true);

    // Act & Assert
    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      authService.register(request);
    });

    assertEquals("Email đã được đăng ký", exception.getMessage());
    verify(customerRepository, never()).save(any());
  }

  @Test
  void testRegister_PhoneNumberAlreadyExists() {
    // Arrange
    RegisterRequestDTO request = new RegisterRequestDTO();
    request.setEmail("a@example.com");
    request.setPhoneNumber("0123456789");

    when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
    when(customerRepository.existsByPhoneNumber(request.getPhoneNumber())).thenReturn(true);

    // Act & Assert
    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      authService.register(request);
    });

    assertEquals("Số điện thoại đã được đăng ký", exception.getMessage());
    verify(customerRepository, never()).save(any());
  }

  @Test
  void testCheckUser_ValidToken_ReturnsClaims() {
    // Arrange
    String fakeToken = "fake.jwt.token";
    TokenRequestDTO request = new TokenRequestDTO();
    request.setToken(fakeToken);

    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", "user@example.com");
    claims.put("role", "CUSTOMER");

    when(jwtUtil.getAllClaimsFromToken(fakeToken)).thenReturn(claims);

    // Act
    ResponseEntity<?> response = authService.checkUser(request);

    // Assert
    assertEquals(200, response.getStatusCodeValue());
    assertEquals(claims, response.getBody());
    verify(jwtUtil).getAllClaimsFromToken(fakeToken);
  }
  @Test
  void refreshToken_shouldReturnNewAccessToken_whenRefreshTokenIsValid() {
    // Arrange
    String refreshToken = "valid-refresh-token";
    String email = "test@example.com";
    String newAccessToken = "new-access-token";

    // Giả lập cookie trong request
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    // Giả lập các dependency
    when(jwtUtil.getUserEmailFromToken(refreshToken)).thenReturn(email);

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
    when(jwtUtil.validateToken(refreshToken, userDetails)).thenReturn(true);

    Customer customer = new Customer();
    customer.setEmail(email);
    when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));

    when(jwtUtil.generateToken(customer)).thenReturn(newAccessToken);

    // Act
    TokenResponseDTO response = authService.refreshToken(request);

    // Assert
    assertNotNull(response);
    assertEquals(newAccessToken, response.getAccessToken());
  }

  @Test
  void refreshToken_shouldThrowException_whenRefreshTokenMissing() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(null);

    assertThrows(UnauthorizedException.class, () -> {
      authService.refreshToken(request);
    });
  }

  @Test
  void refreshToken_shouldThrowException_whenTokenInvalid() {
    String refreshToken = "invalid-token";
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    when(jwtUtil.getUserEmailFromToken(refreshToken)).thenReturn("test@example.com");
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
    when(jwtUtil.validateToken(refreshToken, userDetails)).thenReturn(false);

    assertThrows(UnauthorizedException.class, () -> {
      authService.refreshToken(request);
    });
  }

  @Test
  void refreshToken_shouldThrowException_whenCustomerNotFound() {
    String refreshToken = "valid-token";
    String email = "notfound@example.com";

    Cookie cookie = new Cookie("refreshToken", refreshToken);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    when(jwtUtil.getUserEmailFromToken(refreshToken)).thenReturn(email);

    UserDetails userDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
    when(jwtUtil.validateToken(refreshToken, userDetails)).thenReturn(true);
    when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> {
      authService.refreshToken(request);
    });
  }
  @Test
  void refreshToken_shouldThrowException_whenTokenExpired() {
    // Giả lập trường hợp token hết hạn
    String refreshToken = "expired-refresh-token";
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    when(jwtUtil.getUserEmailFromToken(refreshToken)).thenReturn("test@example.com");
    UserDetails userDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
    when(jwtUtil.validateToken(refreshToken, userDetails)).thenReturn(false);  // Token hết hạn

    assertThrows(UnauthorizedException.class, () -> {
      authService.refreshToken(request);
    });
  }

  @Test
  void refreshToken_shouldThrowException_whenEmailIsEmpty() {
    // Giả lập trường hợp email trống khi lấy từ token
    String refreshToken = "valid-refresh-token";
    Cookie cookie = new Cookie("refreshToken", refreshToken);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    when(jwtUtil.getUserEmailFromToken(refreshToken)).thenReturn("");

    assertThrows(UnauthorizedException.class, () -> {
      authService.refreshToken(request);
    });
  }

  @Test
  void refreshToken_CookieExists() {
    // Arrange
    Cookie cookie = new Cookie("refreshToken", "valid-refresh-token");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});

    // Act
    String refreshToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie c : cookies) {
        if ("refreshToken".equals(c.getName())) {
          refreshToken = c.getValue();
          break;
        }
      }
    }

    // Assert
    assertNotNull(refreshToken);
    assertEquals("valid-refresh-token", refreshToken); // Kiểm tra xem giá trị cookie có chính xác không
  }

}
