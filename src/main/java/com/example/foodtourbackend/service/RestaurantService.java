package com.example.foodtourbackend.service;

import com.example.foodtourbackend.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RestaurantService {
    Restaurant getById(Long id);

    List<Restaurant> filterRestaurants(List<Long> cityIds, List<String> districts, String name);

    List<Restaurant> findAll();

    List<Restaurant> findNearbyRestaurants(double latitude, double longitude, double radius);

    Page<Restaurant> getRestaurants(int page, int size);
}
