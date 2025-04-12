package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.DishesRequestDTO;
import com.example.foodtourbackend.entity.CategoryFood;
import org.springframework.stereotype.Service;

@Service
public interface DishesService {
  CategoryFood addDishes(DishesRequestDTO dishesRequestDTO);

  CategoryFood updateDishes(DishesRequestDTO dishesRequestDTO, Long id);
}
