package com.example.foodtourbackend.repository;

import com.example.foodtourbackend.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
}
