package com.example.foodtourbackend.repository;

import com.example.foodtourbackend.entity.CategoryFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryFoodRepository extends JpaRepository<CategoryFood,Long> {
}
