package com.example.foodtourbackend.repository;

import com.example.foodtourbackend.entity.RestaurantSave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestaurantSaveRepository extends JpaRepository<RestaurantSave, Long> {
  Page<RestaurantSave> findByCustomer_Id(Long userId, Pageable pageable);

  Optional<RestaurantSave> findByCustomer_idAndRestaurant_id(Long userId, Long id);
}
