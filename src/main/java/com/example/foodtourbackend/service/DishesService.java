package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.DishesRequestDTO;
import com.example.foodtourbackend.DTO.response.DishesResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishesService {
  DishesResponseDTO addDishes(DishesRequestDTO dishesRequestDTO);

  DishesResponseDTO updateDishes(DishesRequestDTO dishesRequestDTO, Long id);

  List<?> getAllDishesByRestaurantId(Long restaurantId);

  String delete(Long id);
}
