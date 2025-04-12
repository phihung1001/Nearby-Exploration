package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.DishesRequestDTO;
import com.example.foodtourbackend.DTO.DishesResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface DishesService {
  DishesResponseDTO addDishes(DishesRequestDTO dishesRequestDTO);

  DishesResponseDTO updateDishes(DishesRequestDTO dishesRequestDTO, Long id);
}
