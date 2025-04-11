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

/**
 * Triển khai các chức năng liên quan đến món ăn (CategoryFood)
 * Bao gồm: thêm mới, cập nhật món ăn
 */
@Service
@RequiredArgsConstructor
public class DishesServiceImpl implements DishesService {
  private final CategoryFoodRepository categoryFoodRepository;
  private final CategoryFoodMapper categoryFoodMapper;

  /**
   * Thêm mới một món ăn vào hệ thống
   *
   * @param dishesDTO DTO chứa thông tin món ăn
   * @return CategoryFood - bản ghi món ăn đã được lưu
   */
  @Override
  public CategoryFood addDishes(DishesDTO dishesDTO) {
    CategoryFood newFood = CategoryFoodMapper.INSTANCE.DishesDTOToEntity(dishesDTO);
    return(categoryFoodRepository.save(newFood));
  }

  /**
   * Cập nhật thông tin món ăn dựa trên ID
   *
   * @param dishesDTO DTO chứa thông tin cần cập nhật
   * @param id ID của món ăn cần cập nhật
   * @return CategoryFood - bản ghi món ăn đã được cập nhật
   * @throws NotFoundException nếu không tìm thấy món ăn với ID tương ứng
   */
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
