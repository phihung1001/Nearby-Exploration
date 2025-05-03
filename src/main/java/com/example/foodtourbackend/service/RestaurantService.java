package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.response.ProviderResponseDTO;
import com.example.foodtourbackend.DTO.response.RestaurantResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface RestaurantService {
  RestaurantResponseDTO getById(Long id);

  Page<RestaurantResponseDTO> filterRestaurants(int page, int size, Integer cityId, Integer districtId, String name);

  Page<RestaurantResponseDTO> findNearbyRestaurants(int page,int size, Double latitude, Double longitude, double radius, String name);

  Page<RestaurantResponseDTO> getRestaurants(int page, int size);
  ProviderResponseDTO registerRestaurant(ProviderRequestDTO requestDTO);

  ProviderResponseDTO updateRestaurant(ProviderRequestDTO requestDTO, Long id);

  Page<ProviderResponseDTO> getList(int page, int size);

  Object delete(Long id);

  ResponseEntity<?> save(Long id);
}
