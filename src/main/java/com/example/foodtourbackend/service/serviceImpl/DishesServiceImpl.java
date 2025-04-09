package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.DishesDTO;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.entity.CategoryFood;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.repository.CategoryFoodRepository;
import com.example.foodtourbackend.service.DishesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishesServiceImpl implements DishesService {
  private final CategoryFoodRepository categoryFoodRepository;
  private final CategoryFoodMapper categoryFoodMapper;

  @Override
  public CategoryFood addDishes(DishesDTO dishesDTO) {
    CategoryFood newFood = CategoryFoodMapper.INSTANCE.DishesDTOToEntity(dishesDTO);
    return(categoryFoodRepository.save(newFood));
  }

  @Override
  public CategoryFood updateDishes(DishesDTO dishesDTO, Long id) {
    Optional<CategoryFood> currentFood = categoryFoodRepository.findById(id);
    if(currentFood.isEmpty()){
      throw new NotFoundException("Không tồn tại bản ghi món ăn trong database");
    }
    categoryFoodMapper.UpdateDishesDTOToEntity(dishesDTO, currentFood.get());
    return categoryFoodRepository.save(currentFood.get());
  }
}
