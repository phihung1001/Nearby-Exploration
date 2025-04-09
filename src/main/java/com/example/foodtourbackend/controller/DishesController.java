package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.DishesDTO;
import com.example.foodtourbackend.service.DishesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/dishes")
@RequiredArgsConstructor
public class DishesController {
  private final DishesService dishesService;

  @PostMapping("/add")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> addDishes(@RequestBody DishesDTO dishesDTO) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.addDishes(dishesDTO));
  }

  @PostMapping("update/{id}")
  @PreAuthorize("hasAuthority('PROVIDER')")
  public ResponseEntity<?> updateDishes(
    @RequestBody DishesDTO dishesDTO,
    @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(dishesService.updateDishes(dishesDTO, id));
  }

}
