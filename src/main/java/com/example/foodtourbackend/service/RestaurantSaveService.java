package com.example.foodtourbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantSaveService {
  Page<?> getRestaurants(int page, int size);
}
