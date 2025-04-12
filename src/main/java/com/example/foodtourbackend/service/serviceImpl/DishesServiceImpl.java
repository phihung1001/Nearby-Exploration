package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.DishesRequestDTO;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.CategoryFood;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.repository.CategoryFoodRepository;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.service.DishesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Triển khai các chức năng liên quan đến món ăn (CategoryFood)
 * Bao gồm: thêm mới, cập nhật món ăn
 */
@Service
@RequiredArgsConstructor
public class DishesServiceImpl implements DishesService {
  private final CategoryFoodRepository categoryFoodRepository;
  private final CategoryFoodMapper categoryFoodMapper;
  private final CustomerRepository customerRepository;
  private final RestaurantRepository restaurantRepository;

  /**
   * Thêm món ăn mới vào hệ thống, gắn với nhà hàng mà người dùng đang quản lý.
   *
   * @param dishesRequestDTO Dữ liệu món ăn nhận từ client
   * @return Đối tượng CategoryFood sau khi đã lưu vào database
   * @throws UnauthorizedException nếu người dùng chưa đăng nhập hoặc token không hợp lệ
   * @throws NotFoundException nếu không tìm thấy người dùng hoặc chưa có nhà hàng liên kết
   */
  @Override
  public CategoryFood addDishes(DishesRequestDTO dishesRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    if (dishesRequestDTO.getRestaurantId() == null) {
      throw new IllegalArgumentException("Vui lòng chọn nhà hàng để thêm món ăn");
    }
    if (!restaurantRepository.existsById(dishesRequestDTO.getRestaurantId())) {
      throw new NotFoundException("Nhà hàng không tồn tại");
    }
    Restaurant restaurant = restaurantRepository.findByIdAndCustomer_Id(dishesRequestDTO.getRestaurantId(), userId)
      .orElseThrow(() -> new NotFoundException("Người dùng không có quyền thêm món ăn cho nhà hàng này"));

    CategoryFood newFood = CategoryFoodMapper.INSTANCE.DishesDTOToEntity(dishesRequestDTO);
    // Gán restaurant_id dựa vào nhà hàng của người dùng
    newFood.setRestaurant(restaurant);
    return categoryFoodRepository.save(newFood);
  }

  /**
   * Cập nhật thông tin món ăn dựa trên ID
   *
   * @param dishesRequestDTO DTO chứa thông tin cần cập nhật
   * @param id ID của món ăn cần cập nhật
   * @return CategoryFood - bản ghi món ăn đã được cập nhật
   * @throws NotFoundException nếu không tìm thấy món ăn với ID tương ứng
   */
  @Override
  public CategoryFood updateDishes(DishesRequestDTO dishesRequestDTO, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    // Tìm món ăn theo ID và kiểm tra quyền sở hữu
    CategoryFood currentFood = categoryFoodRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Không tồn tại bản ghi món ăn trong database"));
    if (!currentFood.getRestaurant().getCustomer().getId().equals(userId)) {
      throw new UnauthorizedException("Bạn không có quyền chỉnh sửa món ăn này");
    }

    categoryFoodMapper.UpdateDishesDTOToEntity(dishesRequestDTO, currentFood);
    return categoryFoodRepository.save(currentFood);
  }
}
