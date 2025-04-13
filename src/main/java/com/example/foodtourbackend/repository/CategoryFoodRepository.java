package com.example.foodtourbackend.repository;

import com.example.foodtourbackend.entity.CategoryFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryFoodRepository extends JpaRepository<CategoryFood,Long> {

  List<CategoryFood> findAllByRestaurant_Id(Long restaurantId);
}
