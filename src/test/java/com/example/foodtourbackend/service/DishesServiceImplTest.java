package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.DishesRequestDTO;
import com.example.foodtourbackend.DTO.response.DishesResponseDTO;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.CategoryFood;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.repository.CategoryFoodRepository;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.service.serviceImpl.DishesServiceImpl;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DishesServiceImplTest {

  @Mock
  private CategoryFoodRepository categoryFoodRepository;

  @Mock
  private CategoryFoodMapper categoryFoodMapper;

  @Mock
  private RestaurantRepository restaurantRepository;

  @Mock
  private Authentication authentication;

  @InjectMocks
  private DishesServiceImpl dishesService;

  private DishesRequestDTO dishesRequestDTO;
  private DishesResponseDTO dishesResponseDTO;
  private CategoryFood categoryFood;
  private Restaurant restaurant;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    dishesRequestDTO = new DishesRequestDTO();
    dishesRequestDTO.setRestaurantId(1L);

    dishesResponseDTO = new DishesResponseDTO();
    dishesResponseDTO.setName("Dish Name");

    Customer customer = new Customer();
    customer.setId(123L);

    restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setCustomer(customer);

    categoryFood = new CategoryFood();
    categoryFood.setId(1L);
    categoryFood.setName("Dish Name");
    categoryFood.setRestaurant(restaurant);
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
  void addDishes_ShouldThrowUnauthorizedException_WhenNotAuthenticated() {
    SecurityContextHolder.clearContext();
    assertThrows(UnauthorizedException.class, () -> dishesService.addDishes(dishesRequestDTO));
  }

  @Test
  void addDishes_ShouldThrowNotFoundException_WhenRestaurantDoesNotExist() {
    when(authentication.isAuthenticated()).thenReturn(true);
    when(restaurantRepository.existsById(dishesRequestDTO.getRestaurantId())).thenReturn(false);

    assertThrows(NotFoundException.class, () -> dishesService.addDishes(dishesRequestDTO));
  }
  @Test
  void addDishes_ShouldThrowIllegalArgumentException_WhenNotChoseRestaurant() {
    dishesRequestDTO.setRestaurantId(null);
    assertThrows(IllegalArgumentException.class, () -> dishesService.addDishes(dishesRequestDTO));

  }

  @Test
  void addDishes_ShouldAddDishes_WhenValidData() {

    when(restaurantRepository.existsById(dishesRequestDTO.getRestaurantId())).thenReturn(true);
    when(restaurantRepository.findByIdAndCustomer_Id(dishesRequestDTO.getRestaurantId(), 123L))
      .thenReturn(Optional.of(restaurant));
    when(categoryFoodRepository.save(any(CategoryFood.class))).thenReturn(categoryFood);
    when(categoryFoodMapper.EntityToDishesResponseDTO(categoryFood)).thenReturn(dishesResponseDTO);

    DishesResponseDTO result = dishesService.addDishes(dishesRequestDTO);

    assertNotNull(result);
    assertEquals("Dish Name", result.getName());
  }

  @Test
  void updateDishes_ShouldThrowNotFoundException_WhenDishNotFound() {
    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> dishesService.updateDishes(dishesRequestDTO, 1L));
  }

  @Test
  void updateDishes_ShouldThrowUnauthorizedException_WhenUserDoesNotOwnDish() {
    Customer notOwner = new Customer();
    notOwner.setId(999L);

    Restaurant restaurant = new Restaurant();
    restaurant.setCustomer(notOwner);
    categoryFood.setRestaurant(restaurant);

    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.of(categoryFood));

    assertThrows(UnauthorizedException.class, () -> dishesService.updateDishes(dishesRequestDTO, 1L));
  }
  @Test
  void updateDishes_ShouldThrowUnauthorizedException() {
    SecurityContextHolder.clearContext();
    assertThrows(UnauthorizedException.class, () -> dishesService.updateDishes(dishesRequestDTO, 1L));
  }

  @Test
  void delete_ShouldThrowNotFoundException_WhenDishNotFound() {
    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> dishesService.delete(1L));
  }

  @Test
  void delete_ShouldThrowUnauthorizedException_WhenUserDoesNotOwnDish() {
    Customer notOwner = new Customer();
    notOwner.setId(999L);

    Restaurant restaurant = new Restaurant();
    restaurant.setCustomer(notOwner);
    categoryFood.setRestaurant(restaurant);

    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.of(categoryFood));
    assertThrows(UnauthorizedException.class, () -> dishesService.delete(1L));
  }

  @Test
  void delete_ShouldThrowUnauthorizedException_Not_Logged() {
    SecurityContextHolder.clearContext();
    assertThrows(UnauthorizedException.class, () -> dishesService.delete(1L));
  }

  @Test
  void delete_success() {
    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.of(categoryFood));
    assertDoesNotThrow(() -> dishesService.delete(1L));
    verify(categoryFoodRepository).delete(categoryFood);
  }

  @Test
  void updateDishes_success() {
    when(categoryFoodRepository.findById(anyLong())).thenReturn(Optional.of(categoryFood));
    assertDoesNotThrow(() -> dishesService.updateDishes(dishesRequestDTO,1L));
    verify(categoryFoodRepository).save(categoryFood);
  }


  @Test
  void getAllDishesByRestaurantId_ShouldReturnDishes_WhenValidRestaurantId() {
    when(categoryFoodRepository.findAllByRestaurant_Id(1L)).thenReturn(List.of(categoryFood));
    when(categoryFoodMapper.EntityToDishesResponseDTO(categoryFood)).thenReturn(dishesResponseDTO);

    var result = dishesService.getAllDishesByRestaurantId(1L);

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }

  @Test
  void GetAllDishes_ShouldThrowNotFoundException_WhenRestaurantNotFound() {
    when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> dishesService.GetAllDishes(1L));
  }

  @Test
  void GetAllDishes_ShouldReturnDishes_WhenValidRestaurantId() {
    when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
    when(categoryFoodRepository.findAllByRestaurant_Id(1L)).thenReturn(List.of(categoryFood));
    when(categoryFoodMapper.EntityToDishesResponseDTO(categoryFood)).thenReturn(dishesResponseDTO);

    var result = dishesService.GetAllDishes(1L);

    assertNotNull(result);
    assertFalse(result.isEmpty());
  }
}
