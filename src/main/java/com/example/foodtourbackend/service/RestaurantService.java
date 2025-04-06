package com.example.foodtourbackend.service;

import com.example.foodtourbackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantService {
  Restaurant getById(Long id);

  Page<Restaurant> filterRestaurants(int page, int size, Integer cityId, Integer districtId, String name);

  Page<Restaurant> findNearbyRestaurants(int page,int size, double latitude, double longitude, double radius, String name);

  Page<Restaurant> getRestaurants(int page, int size);
}
