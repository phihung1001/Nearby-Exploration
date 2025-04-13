package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.DishesRequestDTO;
import com.example.foodtourbackend.DTO.DishesResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DishesService {
  DishesResponseDTO addDishes(DishesRequestDTO dishesRequestDTO);

  DishesResponseDTO updateDishes(DishesRequestDTO dishesRequestDTO, Long id);

  List<?> getAllDishesByRestaurantId(Long restaurantId);
}
