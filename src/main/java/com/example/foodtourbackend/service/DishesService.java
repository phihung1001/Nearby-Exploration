package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.DishesDTO;
import com.example.foodtourbackend.entity.CategoryFood;
import org.springframework.stereotype.Service;

@Service
public interface DishesService {
  CategoryFood addDishes(DishesDTO dishesDTO);

  CategoryFood updateDishes(DishesDTO dishesDTO, Long id);
}
