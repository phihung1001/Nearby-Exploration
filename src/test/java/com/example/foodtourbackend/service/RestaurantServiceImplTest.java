package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.response.CommentResponseDTO;
import com.example.foodtourbackend.DTO.response.ProviderResponseDTO;
import com.example.foodtourbackend.DTO.response.RestaurantResponseDTO;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.ErrorImportDataException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.entity.RestaurantSave;
import com.example.foodtourbackend.entity.Reviews;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.mapper.RestaurantMapper;
import com.example.foodtourbackend.mapper.ReviewMapper;
import com.example.foodtourbackend.repository.*;
import com.example.foodtourbackend.service.serviceImpl.RestaurantServiceImpl;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

  @InjectMocks
  private RestaurantServiceImpl restaurantService;

  @Mock
  private RestaurantRepository restaurantRepository;
  @Mock
  private RestaurantMapper restaurantMapper;
  @Mock
  private CategoryFoodMapper categoryFoodMapper;
  @Mock
  private CategoryFoodRepository categoryFoodRepository;
  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private RestaurantSaveRepository restaurantSaveRepository;
  @Mock
  private ReviewRepository reviewRepository;
  @Mock
  private ReviewMapper reviewMapper;

  @BeforeEach
  void setUp() {
    SecurityContextHolder.clearContext();
  }
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
  // Helper method to mock Authentication
  private void mockAuthenticatedUser(Long userId) {
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUserId()).thenReturn(userId);
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  @Test
  void testRegisterRestaurant_Successful() {
    Long userId = 1L;
    mockAuthenticatedUser(userId);

    ProviderRequestDTO dto = new ProviderRequestDTO();
    dto.setEmail("test@email.com");
    dto.setPhone("0123456789");

    Customer customer = new Customer();
    customer.setId(userId);

    Restaurant savedRestaurant = new Restaurant();
    savedRestaurant.setId(100L);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(restaurantRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
    when(restaurantRepository.findByPhone(dto.getPhone())).thenReturn(Optional.empty());

    Restaurant toSave = new Restaurant();
    when(restaurantMapper.ProviderRequestDTOToEntity(dto)).thenReturn(toSave);
    when(restaurantRepository.save(toSave)).thenReturn(savedRestaurant);

    ProviderResponseDTO responseDTO = new ProviderResponseDTO();
    when(restaurantMapper.EntityToProviderResponseDTO(savedRestaurant)).thenReturn(responseDTO);

    ProviderResponseDTO result = restaurantService.registerRestaurant(dto);
    assertEquals(responseDTO, result);

    verify(categoryFoodRepository, never()).saveAll(any());
  }

  @Test
  void testGetById_Success() {
    Long restaurantId = 10L;
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    RestaurantResponseDTO dto = new RestaurantResponseDTO();

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(restaurantMapper.entity2RestaurantResponseDTO(restaurant)).thenReturn(dto);

    RestaurantResponseDTO result = restaurantService.getById(restaurantId);
    assertEquals(dto, result);
  }
  @Test
  void testGetList_WithValidUser_ReturnsPage() {
    Long userId = 1L;
    mockAuthenticatedUser(userId);

    Restaurant restaurant = new Restaurant();
    Page<Restaurant> page = new PageImpl<>(List.of(restaurant));
    ProviderResponseDTO dto = new ProviderResponseDTO();

    when(restaurantRepository.findByCustomer_Id(eq(userId), any(Pageable.class))).thenReturn(page);
    when(restaurantMapper.EntityToProviderResponseDTO(any())).thenReturn(dto);

    Page<ProviderResponseDTO> result = restaurantService.getList(0, 10);
    assertEquals(1, result.getContent().size());
    assertEquals(dto, result.getContent().get(0));
  }
  @Test
  void testDelete_Success() {
    Long userId = 1L;
    Long restaurantId = 100L;
    mockAuthenticatedUser(userId);

    Customer customer = new Customer();
    customer.setId(userId);
    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setCustomer(customer);

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

    String result = restaurantService.delete(restaurantId);
    assertEquals("Xóa nhà hàng thành công", result);
    verify(restaurantRepository).delete(restaurant);
  }
  @Test
  void testGetAllComment_Success() {
    Long restaurantId = 5L;
    Reviews review = new Reviews();
    CommentResponseDTO dto = new CommentResponseDTO();
    Page<Reviews> page = new PageImpl<>(List.of(review));

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));
    when(reviewRepository.findAllByRestaurant_Id(eq(restaurantId), any(Pageable.class))).thenReturn(page);
    when(reviewMapper.reviews2CommentResponseDTO(review)).thenReturn(dto);

    ResponseEntity<List<CommentResponseDTO>> response = restaurantService.getAllComment(0, 10, restaurantId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
  }
  @Test
  public void testGetAllComment_RestaurantNotFound() {
    Long restaurantId = 1L;

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      restaurantService.getAllComment(0, 10, restaurantId);
    });

    assertEquals("Nhà hàng không tồn tại", exception.getMessage());
  }
  @Test
  void testFilterRestaurants_Success() {
    // Dữ liệu đầu vào
    int page = 0;
    int size = 10;
    Integer cityId = 1;
    Integer districtId = 2;
    String name = "Test Restaurant";

    // Mocks
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();

    Page<Restaurant> restaurantPage = new PageImpl<>(List.of(restaurant));

    // Mô phỏng các hành vi cần thiết
    when(restaurantRepository.findByCityIdAndDistrictIdAndName(cityId, districtId, name, PageRequest.of(page, size)))
      .thenReturn(restaurantPage);
    when(restaurantMapper.entity2RestaurantResponseDTO(restaurant)).thenReturn(restaurantResponseDTO);

    // Gọi phương thức filterRestaurants
    Page<RestaurantResponseDTO> result = restaurantService.filterRestaurants(page, size, cityId, districtId, name);

    // Kiểm tra kết quả trả về
    assertEquals(1, result.getContent().size());  // Kiểm tra số lượng phần tử trong kết quả
    assertEquals(restaurantResponseDTO, result.getContent().get(0));  // Kiểm tra đối tượng đầu tiên trong danh sách
  }

  @Test
  void testFilterRestaurants_NoResults() {
    // Dữ liệu đầu vào
    int page = 0;
    int size = 10;
    Integer cityId = 1;
    Integer districtId = 2;
    String name = "Non-Existing Restaurant";

    // Mocks
    Page<Restaurant> emptyPage = new PageImpl<>(List.of());  // Không có nhà hàng nào trong kết quả

    // Mô phỏng hành vi cần thiết
    when(restaurantRepository.findByCityIdAndDistrictIdAndName(cityId, districtId, name, PageRequest.of(page, size)))
      .thenReturn(emptyPage);

    // Gọi phương thức filterRestaurants
    Page<RestaurantResponseDTO> result = restaurantService.filterRestaurants(page, size, cityId, districtId, name);

    // Kiểm tra kết quả trả về
    assertEquals(0, result.getContent().size());  // Không có kết quả nào
  }

  @Test
  void testFindNearbyRestaurants_Success() {
    int page = 0;
    int size = 10;
    Double latitude = 21.028511;
    Double longitude = 105.804817;
    double radius = 5.0;
    String name = "Bún chả";

    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Bún chả Hương Liên");

    RestaurantResponseDTO dto = new RestaurantResponseDTO();
    dto.setId(1L);
    dto.setName("Bún chả Hương Liên");

    Page<Restaurant> pageResult = new PageImpl<>(List.of(restaurant), PageRequest.of(page, size), 1);

    when(restaurantRepository.findNearbyRestaurants(latitude, longitude, radius, name, PageRequest.of(page, size)))
      .thenReturn(pageResult);
    when(restaurantMapper.entity2RestaurantResponseDTO(restaurant)).thenReturn(dto);

    Page<RestaurantResponseDTO> result = restaurantService.findNearbyRestaurants(page, size, latitude, longitude, radius, name);

    assertEquals(1, result.getContent().size());
    assertEquals(dto, result.getContent().get(0));
    verify(restaurantRepository).findNearbyRestaurants(latitude, longitude, radius, name, PageRequest.of(page, size));
    verify(restaurantMapper).entity2RestaurantResponseDTO(restaurant);
  }

  @Test
  void testFindNearbyRestaurants_LatitudeNull_ThrowsException() {
    int page = 0;
    int size = 10;
    Double latitude = null;
    Double longitude = 105.804817;
    double radius = 5.0;
    String name = "Bún chả";

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      restaurantService.findNearbyRestaurants(page, size, latitude, longitude, radius, name);
    });

    assertEquals("Cho phép hệ thống truy cập vị trí của bạn để lấy danh sách nhà hàng.", exception.getMessage());
  }

  @Test
  void testFindNearbyRestaurants_LongitudeNull_ThrowsException() {
    int page = 0;
    int size = 10;
    Double latitude = 21.028511;
    Double longitude = null;
    double radius = 5.0;
    String name = "Bún chả";

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      restaurantService.findNearbyRestaurants(page, size, latitude, longitude, radius, name);
    });

    assertEquals("Cho phép hệ thống truy cập vị trí của bạn để lấy danh sách nhà hàng.", exception.getMessage());
  }

  @Test
  void testFindNearbyRestaurants_EmptyResult() {
    int page = 0;
    int size = 10;
    Double latitude = 21.028511;
    Double longitude = 105.804817;
    double radius = 5.0;
    String name = "Không tồn tại";

    Page<Restaurant> emptyPage = new PageImpl<>(List.of(), PageRequest.of(page, size), 0);

    when(restaurantRepository.findNearbyRestaurants(latitude, longitude, radius, name, PageRequest.of(page, size)))
      .thenReturn(emptyPage);

    Page<RestaurantResponseDTO> result = restaurantService.findNearbyRestaurants(page, size, latitude, longitude, radius, name);

    assertEquals(0, result.getContent().size());
    verify(restaurantRepository).findNearbyRestaurants(latitude, longitude, radius, name, PageRequest.of(page, size));
  }
  @Test
  void testGetRestaurants_Success() {
    // Arrange
    int page = 0;
    int size = 5;

    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setName("Nhà hàng A");

    RestaurantResponseDTO dto = new RestaurantResponseDTO();
    dto.setId(1L);
    dto.setName("Nhà hàng A");

    Page<Restaurant> restaurantPage = new PageImpl<>(List.of(restaurant), PageRequest.of(page, size), 1);

    when(restaurantRepository.findAll(PageRequest.of(page, size))).thenReturn(restaurantPage);
    when(restaurantMapper.entity2RestaurantResponseDTO(restaurant)).thenReturn(dto);

    // Act
    Page<RestaurantResponseDTO> result = restaurantService.getRestaurants(page, size);

    // Assert
    assertEquals(1, result.getContent().size());
    assertEquals(dto, result.getContent().get(0));
    verify(restaurantRepository).findAll(PageRequest.of(page, size));
    verify(restaurantMapper).entity2RestaurantResponseDTO(restaurant);
  }

  @Test
  void testGetRestaurants_EmptyResult() {
    // Arrange
    int page = 0;
    int size = 5;
    Page<Restaurant> emptyPage = new PageImpl<>(List.of(), PageRequest.of(page, size), 0);

    when(restaurantRepository.findAll(PageRequest.of(page, size))).thenReturn(emptyPage);

    // Act
    Page<RestaurantResponseDTO> result = restaurantService.getRestaurants(page, size);

    // Assert
    assertTrue(result.getContent().isEmpty());
    verify(restaurantRepository).findAll(PageRequest.of(page, size));
    verifyNoInteractions(restaurantMapper); // không nên gọi mapper khi không có entity
  }
  @Test
  void testUpdateRestaurant_Success() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setEmail("new@email.com");
    requestDTO.setPhone("0123456789");

    Restaurant existingRestaurant = new Restaurant();
    existingRestaurant.setId(restaurantId);
    Customer owner = new Customer();
    owner.setId(userId);
    existingRestaurant.setCustomer(owner);

    ProviderResponseDTO responseDTO = new ProviderResponseDTO();

    when(restaurantRepository.findByIdAndCustomer_Id(restaurantId, userId))
      .thenReturn(Optional.of(existingRestaurant));
    when(restaurantRepository.findByEmail(requestDTO.getEmail()))
      .thenReturn(Optional.empty());
    when(restaurantRepository.findByPhone(requestDTO.getPhone()))
      .thenReturn(Optional.empty());

    doNothing().when(restaurantMapper).UpdateProviderRequestDTOToEntity(requestDTO, existingRestaurant);
    when(restaurantRepository.save(existingRestaurant)).thenReturn(existingRestaurant);
    when(restaurantMapper.EntityToProviderResponseDTO(existingRestaurant)).thenReturn(responseDTO);

    ProviderResponseDTO result = restaurantService.updateRestaurant(requestDTO, restaurantId);

    assertEquals(responseDTO, result);
    verify(restaurantRepository).save(existingRestaurant);
  }

  @Test
  void testUpdateRestaurant_UnauthenticatedUser_ThrowsException() {
    SecurityContextHolder.clearContext(); // Không set authentication

    ProviderRequestDTO dto = new ProviderRequestDTO();

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
      restaurantService.updateRestaurant(dto, 1L)
    );

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void testUpdateRestaurant_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false); // chưa xác thực
    SecurityContextHolder.getContext().setAuthentication(authentication);

    ProviderRequestDTO dto = new ProviderRequestDTO();

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
      restaurantService.updateRestaurant(dto, 1L)
    );

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void testUpdateRestaurant_EmailNull_ShouldSkipEmailCheck() {

    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setEmail(null); // email null

    Restaurant restaurant = new Restaurant();
    restaurant.setId(10L);
    restaurant.setCustomer(new Customer());

    when(restaurantRepository.findByIdAndCustomer_Id(10L, 1L)).thenReturn(Optional.of(restaurant));
    when(restaurantRepository.save(any())).thenReturn(restaurant);
    when(restaurantMapper.EntityToProviderResponseDTO(any())).thenReturn(new ProviderResponseDTO());

    ProviderResponseDTO response = restaurantService.updateRestaurant(requestDTO, 10L);
    assertNotNull(response);
  }
  @Test
  void testUpdateRestaurant_EmailEmpty_ShouldSkipEmailCheck() {

    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setEmail(""); // email rỗng

    Restaurant restaurant = new Restaurant();
    restaurant.setId(10L);
    restaurant.setCustomer(new Customer());

    when(restaurantRepository.findByIdAndCustomer_Id(10L, 1L)).thenReturn(Optional.of(restaurant));
    when(restaurantRepository.save(any())).thenReturn(restaurant);
    when(restaurantMapper.EntityToProviderResponseDTO(any())).thenReturn(new ProviderResponseDTO());

    ProviderResponseDTO response = restaurantService.updateRestaurant(requestDTO, 10L);
    assertNotNull(response);
  }

  @Test
  void testUpdateRestaurant_EmailValidAndNotDuplicate_ShouldPass() {

    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setEmail("valid@email.com");

    Restaurant restaurant = new Restaurant();
    restaurant.setId(10L);
    restaurant.setCustomer(new Customer());

    when(restaurantRepository.findByIdAndCustomer_Id(10L, 1L)).thenReturn(Optional.of(restaurant));
    when(restaurantRepository.findByEmail("valid@email.com")).thenReturn(Optional.empty());
    when(restaurantRepository.save(any())).thenReturn(restaurant);
    when(restaurantMapper.EntityToProviderResponseDTO(any())).thenReturn(new ProviderResponseDTO());

    ProviderResponseDTO response = restaurantService.updateRestaurant(requestDTO, 10L);
    assertNotNull(response);
  }



  @Test
  void testUpdateRestaurant_NotOwnedRestaurant_ThrowsException() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    when(restaurantRepository.findByIdAndCustomer_Id(restaurantId, userId))
      .thenReturn(Optional.empty());

    ProviderRequestDTO dto = new ProviderRequestDTO();

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
      restaurantService.updateRestaurant(dto, restaurantId)
    );

    assertEquals("Nhà hàng không tồn tại hoặc không thuộc quyền sở hữu.", exception.getMessage());
  }

  @Test
  void testUpdateRestaurant_DuplicateEmail_ThrowsException() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    ProviderRequestDTO dto = new ProviderRequestDTO();
    dto.setEmail("duplicate@email.com");

    Restaurant current = new Restaurant();
    current.setId(restaurantId);
    Customer owner = new Customer();
    owner.setId(userId);
    current.setCustomer(owner);

    Restaurant other = new Restaurant();
    other.setId(999L); // khác id

    when(restaurantRepository.findByIdAndCustomer_Id(restaurantId, userId)).thenReturn(Optional.of(current));
    when(restaurantRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(other));

    DuplicateException exception = assertThrows(DuplicateException.class, () ->
      restaurantService.updateRestaurant(dto, restaurantId)
    );

    assertEquals("Email đã được đăng ký cho một nhà hàng khác.", exception.getMessage());
  }

  @Test
  void testUpdateRestaurant_DuplicatePhone_ThrowsException() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    ProviderRequestDTO dto = new ProviderRequestDTO();
    dto.setPhone("0123456789");

    Restaurant current = new Restaurant();
    current.setId(restaurantId);
    Customer owner = new Customer();
    owner.setId(userId);
    current.setCustomer(owner);

    Restaurant other = new Restaurant();
    other.setId(999L); // khác id

    when(restaurantRepository.findByIdAndCustomer_Id(restaurantId, userId)).thenReturn(Optional.of(current));
    when(restaurantRepository.findByPhone(dto.getPhone())).thenReturn(Optional.of(other));

    DuplicateException exception = assertThrows(DuplicateException.class, () ->
      restaurantService.updateRestaurant(dto, restaurantId)
    );

    assertEquals("Số điện thoại đã được đăng ký cho một khách hàng khác.", exception.getMessage());
  }
  @Test
  void testSave_Success() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    Customer customer = new Customer();
    customer.setId(userId);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);

    when(customerRepository.findById(userId)).thenReturn(Optional.of(customer));
    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(restaurantSaveRepository.findByCustomer_idAndRestaurant_id(userId, restaurantId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = restaurantService.save(restaurantId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Lưu thành công", ((Map<?, ?>) response.getBody()).get("message"));

    verify(restaurantSaveRepository).save(any(RestaurantSave.class));
  }

  @Test
  void testSave_UnauthenticatedUser_ReturnsUnauthorized() {
    SecurityContextHolder.clearContext(); // Clear authentication

    ResponseEntity<?> response = restaurantService.save(1L);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Vui lòng đăng nhập để lưu nhà hàng", ((Map<?, ?>) response.getBody()).get("message"));
  }

  @Test
  void updateRestaurant_UnauthenticatedUser_ReturnsUnauthorized() {
    SecurityContextHolder.clearContext(); // Clear authentication
    ProviderRequestDTO dto = new ProviderRequestDTO();
    dto.setEmail("duplicate@email.com");

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
      restaurantService.updateRestaurant(dto,1L)
    );
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void deleteRestaurant_UnauthenticatedUser_ReturnsUnauthorized() {
    SecurityContextHolder.clearContext(); // Clear authentication
    ProviderRequestDTO dto = new ProviderRequestDTO();
    dto.setEmail("duplicate@email.com");

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
      restaurantService.delete(1L)
    );
    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void testDelete_NotOwner_ThrowsUnauthorizedException() {
    // Giả lập user đăng nhập với id = 1
    Long loggedInUserId = 1L;
    Long restaurantOwnerId = 2L;
    Long restaurantId = 10L;

    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUserId()).thenReturn(loggedInUserId);

    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Nhà hàng thuộc về user khác
    Customer owner = new Customer();
    owner.setId(restaurantOwnerId);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);
    restaurant.setCustomer(owner);

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.delete(restaurantId);
    });

    assertEquals("Bạn không có quyền xóa nhà hàng này", exception.getMessage());
  }

  @Test
  void testDelete_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false); // chưa xác thực
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.delete(1L);
    });

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }


  @Test
  void testSave_Unauthenticated_ReturnsUnauthorized() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false); // not authenticated
    SecurityContextHolder.getContext().setAuthentication(authentication);

    ResponseEntity<?> response = restaurantService.save(1L);

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("Vui lòng đăng nhập để lưu nhà hàng", ((Map<?, ?>) response.getBody()).get("message"));
  }

  @Test
  void testSave_RestaurantNotFound_ReturnsBadRequest() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

    ResponseEntity<?> response = restaurantService.save(restaurantId);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Nhà hàng không tồn tại", ((Map<?, ?>) response.getBody()).get("message"));
  }

  @Test
  void testSave_RestaurantAlreadySaved_ThrowsErrorImportDataException() {
    Long restaurantId = 100L;
    Long userId = 1L;

    mockAuthenticatedUser(userId);

    RestaurantSave alreadySaved = new RestaurantSave();

    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(new Restaurant()));
    when(customerRepository.findById(userId)).thenReturn(Optional.of(new Customer()));
    when(restaurantSaveRepository.findByCustomer_idAndRestaurant_id(userId, restaurantId)).thenReturn(Optional.of(alreadySaved));

    ErrorImportDataException exception = assertThrows(ErrorImportDataException.class, () ->
      restaurantService.save(restaurantId)
    );

    assertEquals("Nhà hàng đã lưu trước đó", exception.getMessage());
  }
  @Test
  void testGetList_AuthenticationNull_ThrowsUnauthorizedException() {
    SecurityContextHolder.clearContext(); // Không set Authentication

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.getList(0, 10);
    });

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void testGetList_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.getList(0, 10);
    });

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }
  @Test
  void testGetList_NoRestaurantsFound_ThrowsNotFoundException() {
    // Giả lập user đã đăng nhập
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUserId()).thenReturn(1L);

    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Page<Restaurant> emptyPage = Page.empty();
    when(restaurantRepository.findByCustomer_Id(eq(1L), any(Pageable.class)))
      .thenReturn(emptyPage);

    NotFoundException exception = assertThrows(NotFoundException.class, () -> {
      restaurantService.getList(0, 10);
    });

    assertEquals("Bạn chưa quản lý nhà hàng nào cả.", exception.getMessage());
  }

  @Test
  void testRegisterRestaurant_Unauthenticated_ThrowsUnauthorizedException() {
    SecurityContextHolder.clearContext(); // Không có authentication

    ProviderRequestDTO requestDTO = new ProviderRequestDTO();

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.registerRestaurant(requestDTO);
    });

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }

  @Test
  void testRegisterRestaurant_AuthenticationNotAuthenticated_ThrowsUnauthorizedException() {
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(false);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    ProviderRequestDTO requestDTO = new ProviderRequestDTO();

    UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
      restaurantService.registerRestaurant(requestDTO);
    });

    assertEquals("Chưa đăng nhập hoặc token không hợp lệ", exception.getMessage());
  }
  @Test
  void testRegisterRestaurant_DuplicateEmail_ThrowsDuplicateException() {
    // Setup authentication
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUserId()).thenReturn(1L);
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Dữ liệu đầu vào
    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setEmail("duplicate@example.com");

    // Giả lập email đã tồn tại
    when(restaurantRepository.findByEmail("duplicate@example.com"))
      .thenReturn(Optional.of(new Restaurant()));
    when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));

    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      restaurantService.registerRestaurant(requestDTO);
    });

    assertEquals("Email đã được đăng ký cho một nhà hàng khác.", exception.getMessage());
  }
  @Test
  void testRegisterRestaurant_DuplicatePhone_ThrowsDuplicateException() {
    // Setup authentication
    UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
    when(userDetails.getUserId()).thenReturn(1L);
    Authentication authentication = mock(Authentication.class);
    when(authentication.isAuthenticated()).thenReturn(true);
    when(authentication.getPrincipal()).thenReturn(userDetails);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    // Dữ liệu đầu vào
    ProviderRequestDTO requestDTO = new ProviderRequestDTO();
    requestDTO.setPhone("0123456789");

    when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
    when(restaurantRepository.findByPhone("0123456789"))
      .thenReturn(Optional.of(new Restaurant()));

    DuplicateException exception = assertThrows(DuplicateException.class, () -> {
      restaurantService.registerRestaurant(requestDTO);
    });

    assertEquals("Số điện thoại đã được đăng ký cho một khách hàng khác.", exception.getMessage());
  }


}