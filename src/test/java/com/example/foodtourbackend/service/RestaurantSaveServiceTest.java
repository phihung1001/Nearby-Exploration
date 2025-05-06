package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.response.RestaurantResponseDTO;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.RestaurantSave;
import com.example.foodtourbackend.mapper.RestaurantMapper;
import com.example.foodtourbackend.repository.RestaurantSaveRepository;
import com.example.foodtourbackend.service.serviceImpl.RestaurantSaveServiceImpl;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RestaurantSaveServiceTest {
  @Mock
  private RestaurantSaveRepository restaurantSaveRepository;

  @Mock
  private RestaurantMapper restaurantMapper;
  @InjectMocks
  private RestaurantSaveServiceImpl restaurantSaveService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }
  @BeforeEach
  void setupAuthentication() {
    UserDetailsImpl userDetails = new UserDetailsImpl(123L,
      "testuser",
      "password",
      List.of(new SimpleGrantedAuthority("CUSTOMER")));

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
      userDetails, null, userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @Test
  void getRestaurants_shouldThrowUnauthorizedException_whenNotAuthenticated() {
    SecurityContextHolder.clearContext();
    assertThrows(UnauthorizedException.class, () -> restaurantSaveService.getRestaurants(0, 10));
  }

  @Test
  void getRestaurants_shouldThrowUnauthorizedException_whenAuthenticationIsNotAuthenticated() {
    Authentication mockAuth = mock(Authentication.class);
    when(mockAuth.isAuthenticated()).thenReturn(false);
    SecurityContextHolder.getContext().setAuthentication(mockAuth);

    assertThrows(UnauthorizedException.class, () -> restaurantSaveService.getRestaurants(0, 10));
  }

  @Test
  void getRestaurants_shouldReturnPaginatedRestaurants_whenAuthenticated() {
    // Arrange
    Long userId = 123L;
    RestaurantSave restaurantSave = new RestaurantSave();
    RestaurantResponseDTO responseDTO = new RestaurantResponseDTO();

    Page<RestaurantSave> mockPage = new PageImpl<>(Collections.singletonList(restaurantSave));

    when(restaurantSaveRepository.findByCustomer_Id(eq(userId), any(Pageable.class))).thenReturn(mockPage);
    when(restaurantMapper.entitySave2RestaurantResponseDTO(restaurantSave)).thenReturn(responseDTO);

    // Act
    Page<RestaurantResponseDTO> result = restaurantSaveService.getRestaurants(0, 10);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    verify(restaurantSaveRepository).findByCustomer_Id(eq(userId), any(Pageable.class));
    verify(restaurantMapper).entitySave2RestaurantResponseDTO(restaurantSave);
  }
}
