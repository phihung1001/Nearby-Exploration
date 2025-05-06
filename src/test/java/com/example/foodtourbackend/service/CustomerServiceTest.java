package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.CommentDTO;
import com.example.foodtourbackend.DTO.request.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.request.UpdatePasswordRequestDTO;
import com.example.foodtourbackend.DTO.response.ApiResponse;
import com.example.foodtourbackend.DTO.response.CommentResponseDTO;
import com.example.foodtourbackend.DTO.response.CustomerResponseDTO;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.entity.Reviews;
import com.example.foodtourbackend.mapper.CustomerMapper;
import com.example.foodtourbackend.mapper.ReviewMapper;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.repository.ReviewRepository;
import com.example.foodtourbackend.service.serviceImpl.CustomerServiceImpl;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @Value("${ai.service.url}")
  private String aiServiceUrl;
  @InjectMocks
  private CustomerServiceImpl customerService;

  @Mock
  private RestaurantRepository restaurantRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewMapper reviewMapper;

  @Mock
  private CustomerMapper customerMapper;

  @Mock
  private Customer customer;

  @Mock
  private CustomerResponseDTO customerResponseDTO;
  @Mock
  private Authentication authentication;
  @Mock
  private UserDetailsImpl userDetails;

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
  void comment_success() {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO("Rất ngon", 4.5);

    Customer customer = new Customer();
    customer.setId(1L);
    customer.setFullName("Test User");

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setName("Test Restaurant");

    Reviews review = new Reviews();
    review.setComment("Rất ngon");
    review.setAvgRatingText(4.5);

    CommentResponseDTO responseDTO = new CommentResponseDTO();
    responseDTO.setComment("Rất ngon");
    responseDTO.setAvgRatingText(4.5);

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
    when(reviewMapper.commentDTO2Reviews(commentDTO)).thenReturn(review);
    when(reviewMapper.reviews2CommentResponseDTO(any())).thenReturn(responseDTO);

    ResponseEntity<ApiResponse<CommentResponseDTO>> response = customerService.comment(restaurantId, commentDTO);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals("Rất ngon", response.getBody().getData().getComment());
    assertEquals(4.5, response.getBody().getData().getAvgRatingText());
    assertEquals("Bình luận thành công", response.getBody().getMessage());

    verify(reviewRepository, times(1)).save(any(Reviews.class));
  }

  @Test
  void comment_user_not_logged_in() {
    // Arrange
    SecurityContextHolder.clearContext();  // Simulate no user logged in.
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO("Rất ngon", 4.5);

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.comment(restaurantId, commentDTO);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void comment_restaurant_not_found() {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO("Rất ngon", 4.5);

    Customer customer = new Customer();
    customer.setId(1L);
    customer.setFullName("Test User");

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      customerService.comment(restaurantId, commentDTO);
    });
    assertEquals("Nhà hàng không tồn tại", exception.getMessage());
  }

  @Test
  void comment_customer_not_found() {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO("Rất ngon", 4.5);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setName("Test Restaurant");

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(customerRepository.findById(1L)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      customerService.comment(restaurantId, commentDTO);
    });
    assertEquals("Bạn chưa đăng nhập", exception.getMessage());
  }

  @Test
  void upgrade_unauthorized() {
    // Arrange
    SecurityContextHolder.clearContext();

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.upgrade();
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void upgrade_user_not_found() {
    // Arrange
    Long userId = 1L;
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
      new UserDetailsImpl(userId, "testuser", "password", List.of(new SimpleGrantedAuthority("CUSTOMER"))),
      null, List.of(new SimpleGrantedAuthority("CUSTOMER"))));

    when(customerRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      customerService.upgrade();
    });
    assertEquals("Không tìm thấy người dùng", exception.getMessage());
  }

  @Test
  void upgrade_success() {
    // Arrange
    Long userId = 1L;
    Customer customer = new Customer(userId, 30, "Test User", "123456789", "testuser@example.com", "Some Address", "CUSTOMER", "password", "Male", null, null, null);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(customerRepository.save(customer)).thenReturn(customer);

    // Act
    Map<String, String> response = customerService.upgrade();

    // Assert
    assertEquals("Nâng quyền thành công", response.get("message"));
    assertEquals("PROVIDER", customer.getRole());  // Verify the role is updated

    verify(customerRepository, times(1)).save(customer);  // Verify that save was called once
  }

  @Test
  void updatePassword_unauthorized_not_authenticated() {
    // Arrange
    SecurityContextHolder.clearContext();  // Simulate unauthenticated user

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword","newpassword");

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.updatePassword(1L, request);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void updatePassword_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false); // chưa xác thực
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword","newpassword");

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.updatePassword(1L, request);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void updatePassword_unauthorized_invalid_user() {
    // Arrange
    Long userId = 1L;
    Long otherUserId = 2L;

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword","newpassword");

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.updatePassword(otherUserId, request);
    });
    assertEquals("Bạn không có quyền chỉnh sửa thông tin người dùng này", exception.getMessage());
  }

  @Test
  void updatePassword_user_not_found() {
    // Arrange
    Long userId = 1L;
    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword","newpassword");

    when(customerRepository.findById(userId)).thenReturn(Optional.empty());

    // Act & Assert
    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      customerService.updatePassword(userId, request);
    });
    assertEquals("Không tìm thấy người dùng", exception.getMessage());
  }

  @Test
  void updatePassword_incorrect_old_password() {
    // Arrange
    Long userId = 1L;
    Customer customer = new Customer(userId, 30, "Test User", "123456789", "testuser@example.com", "Some Address", "CUSTOMER", "oldpassword", "Male", null, null, null);

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("password", "newpassword", "newpassword");

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));

    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerService.updatePassword(userId, request);
    });

    // Kiểm tra thông báo lỗi có khớp không
    assertEquals("Mật khẩu cũ không đúng", exception.getMessage());
  }


  @Test
  void updatePassword_new_password_empty() {
    // Arrange
    Long userId = 1L;
    Customer customer = new Customer(userId, 30, "Test User", "123456789", "testuser@example.com", "Some Address", "CUSTOMER", "hashedOldPassword", "Male", null, null, null);

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "","");

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerService.updatePassword(userId, request);
    });
    assertEquals("Mật khẩu mới không được để trống", exception.getMessage());
  }

  @Test
  void updatePassword_same_as_old_password() {
    // Arrange
    Long userId = 1L;
    Customer customer = new Customer(userId, 30, "Test User", "123456789", "testuser@example.com", "Some Address", "CUSTOMER", "hashedOldPassword", "Male", null, null, null);

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword","newpassword");


    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerService.updatePassword(userId, request);
    });
    assertEquals("Mật khẩu mới phải khác mật khẩu cũ", exception.getMessage());
  }

  @Test
  void updatePassword_success() {
    // Arrange
    Long userId = 1L;
    String encodedOldPassword = new BCryptPasswordEncoder().encode("oldpassword");

    Customer customer = new Customer(userId, 30, "Test User", "123456789", "testuser@example.com", "Some Address", "CUSTOMER", encodedOldPassword, "Male", null, null, null);

    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO("oldpassword", "newpassword", "newpassword");

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(passwordEncoder.matches("oldpassword", encodedOldPassword)).thenReturn(true);
    when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedNewPassword");
    when(customerRepository.save(customer)).thenReturn(customer);

    // Act
    Map<String, Object> response = customerService.updatePassword(userId, request);

    // Assert
    assertEquals("Đổi mật khẩu thành công", response.get("message"));
    assertEquals("encodedNewPassword", customer.getPassword());  // Verify password is updated
    verify(customerRepository, times(1)).save(customer);  // Verify that save was called once
  }

  @Test
  void updateCustomer_success() {
    // Arrange
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(
      25, "Updated User", "newpassword", "0987654321",
      "newemail@example.com", "New Address", "Male"
    );
    Customer existingCustomer = new Customer(
      userId, 25, "Test User", "123456789",
      "testuser@example.com", "Old Address",
      "CUSTOMER", "oldpassword", "Male",
      null, null, null
    );

    // Mock các phương thức của customerRepository
    when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));
    when(customerRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.empty());
    when(customerRepository.findByPhoneNumber(requestDTO.getPhoneNumber())).thenReturn(Optional.empty());

    // Giả lập save() trả về customer đã được cập nhật
    when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
      Customer customerToSave = invocation.getArgument(0);
      customerToSave.setFullName(requestDTO.getFullName());
      customerToSave.setEmail(requestDTO.getEmail());
      customerToSave.setPhoneNumber(requestDTO.getPhoneNumber());
      customerToSave.setAddress(requestDTO.getAddress());
      customerToSave.setGender(requestDTO.getGender());
      return customerToSave;
    });

    // Giả lập mapper trả về CustomerResponseDTO từ Customer
    when(customerMapper.entityToResponse(any(Customer.class))).thenReturn(new CustomerResponseDTO(
      userId, 25, "Updated User", "0987654321",
      "newemail@example.com", "New Address", "Male"));

    // Act
    CustomerResponseDTO responseDTO = customerService.update(userId, requestDTO);

    // Assert
    assertNotNull(responseDTO, "ResponseDTO should not be null");
    assertEquals("Updated User", responseDTO.getFullName());
    assertEquals(25, responseDTO.getAge());
    assertEquals("newemail@example.com", responseDTO.getEmail());
    assertEquals("0987654321", responseDTO.getPhoneNumber());
    assertEquals("New Address", responseDTO.getAddress());
    assertEquals("Male", responseDTO.getGender());

    // Kiểm tra rằng phương thức save() đã được gọi chính xác 1 lần
    verify(customerRepository, times(1)).save(any(Customer.class));
  }

  @Test
  void updateCustomer_email_duplicate() {
    // Arrange
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(25, "Updated User", "newpassword", "0987654321", "newemail@example.com", "New Address", "Male");
    Customer existingCustomer = new Customer(userId, 25, "Test User", "123456789", "testuser@example.com", "Old Address", "CUSTOMER", "oldpassword", "Male", null, null, null);
    Customer existingEmailCustomer = new Customer(2L, 30, "Another User", "987654321", "newemail@example.com", "Address", "CUSTOMER", "password", "Female", null, null, null);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));
    when(customerRepository.findByEmail(requestDTO.getEmail())).thenReturn(Optional.of(existingEmailCustomer));

    // Act & Assert
    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Email đã được đăng ký cho một khách hàng khác.", exception.getMessage());
  }
  @Test
  void updateCustomer_AuthenticationNull_ThrowsUnauthorizedException() {
    SecurityContextHolder.clearContext();
    CustomerRequestDTO request = new CustomerRequestDTO();

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.update(1L, request);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void updateCustomer_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false); // chưa xác thực
    SecurityContextHolder.getContext().setAuthentication(authentication);
    CustomerRequestDTO request = new CustomerRequestDTO();

    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.update(1L, request);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }
  @Test
  void updateCustomer_phone_duplicate() {
    // Arrange
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(25, "Updated User", "newpassword", "0987654321", "newemail@example.com", "New Address", "Male");
    Customer existingCustomer = new Customer(userId, 25, "Test User", "123456789", "testuser@example.com", "Old Address", "CUSTOMER", "oldpassword", "Male", null, null, null);
    Customer existingPhoneCustomer = new Customer(2L, 30, "Another User", "987654321", "anotheremail@example.com", "Address", "CUSTOMER", "password", "Female", null, null, null);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));
    when(customerRepository.findByPhoneNumber(requestDTO.getPhoneNumber())).thenReturn(Optional.of(existingPhoneCustomer));

    // Act & Assert
    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Số điện thoại đã được đăng ký cho một khách hàng khác.", exception.getMessage());
  }

  @Test
  void updateCustomer_invalid_age() {
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(-1, "Updated User", "newpassword", "0987654321", "newemail@example.com", "New Address", "Male");
    Customer existingCustomer = new Customer(userId, 25, "Test User", "123456789", "testuser@example.com", "Old Address", "CUSTOMER", "oldpassword", "Male", null, null, null);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Độ tuổi không phù hợp", exception.getMessage());
  }

  @Test
  void updateCustomer_invalid_phone_number() {
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(25, "Updated User", "newpassword", "12345", "newemail@example.com", "New Address", "Male");
    Customer existingCustomer = new Customer(userId, 25, "Test User", "123456789", "testuser@example.com", "Old Address", "CUSTOMER", "oldpassword", "Male", null, null, null);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(existingCustomer));

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Số điện thoại không đúng định dạng ", exception.getMessage());
  }

  @Test
  void searchByImage_success() throws IOException {
    // Arrange: mock MultipartFile
    MultipartFile file = mock(MultipartFile.class);
    byte[] fileContent = "image content".getBytes();
    when(file.getBytes()).thenReturn(fileContent);

    // Mock RestTemplate để trả về phản hồi từ AI Service
    RestTemplate restTemplate = mock(RestTemplate.class);
    String expectedResponse = "Image recognized successfully";
    ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);

    when(restTemplate.postForEntity(eq(aiServiceUrl), any(HttpEntity.class), eq(String.class)))
      .thenReturn(responseEntity);

    // Tiêm RestTemplate mock vào CustomerService
    customerService = new CustomerServiceImpl(
      customerRepository,
      restaurantRepository,
      reviewRepository,
      customerMapper,
      reviewMapper,
      passwordEncoder
    );
    ReflectionTestUtils.setField(customerService, "restTemplate", restTemplate);

    // Act: gọi phương thức searchByImage
    String result = customerService.searchByImage(file);

    // Assert: kiểm tra kết quả trả về
    assertEquals(expectedResponse, result);
  }

  @Test
  public void testGetById_Success() {
    // Arrange
    Long customerId = 1L;
    Long userId = 1L;

    when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
    when(customerMapper.entityToResponse(customer)).thenReturn(customerResponseDTO);

    // Act
    CustomerResponseDTO result = customerService.getById(customerId);

    // Assert
    assertNotNull(result);
    verify(customerRepository, times(1)).findById(customerId);
    verify(customerMapper, times(1)).entityToResponse(customer);
  }


  @Test
  public void testGetById_NotFound() {
    // Arrange
    Long customerId = 1L;
    when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> {
      customerService.getById(customerId);
    });
  }

  @Test
  public void testGetById_Unauthorized() {
    // Arrange
    Long customerId = 1L;
    Long userId = 2L;
    // Act & Assert
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.getById(userId);
    });

    assertEquals("Bạn không có quyền truy cập hồ sơ này.", exception.getMessage());
  }

  @Test
  void updateCustomer_fail_not_logged() {
    SecurityContextHolder.clearContext();
    Long userId = 1L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(
      25, "Updated User", "newpassword", "0987654321",
      "newemail@example.com", "New Address", "Male"
    );
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void updateCustomer_fail_unauthorized_invalid_user() {
    Long userId = 2L;
    CustomerRequestDTO requestDTO = new CustomerRequestDTO(
      25, "Updated User", "newpassword", "0987654321",
      "newemail@example.com", "New Address", "Male"
    );
    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      customerService.update(userId, requestDTO);
    });
    assertEquals("Bạn không có quyền chỉnh sửa thông tin người dùng này", exception.getMessage());
  }
}
