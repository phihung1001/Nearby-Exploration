
package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.response.RestaurantResponseDTO;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.RestaurantSave;
import com.example.foodtourbackend.mapper.RestaurantMapper;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.repository.RestaurantSaveRepository;
import com.example.foodtourbackend.service.RestaurantSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantSaveServiceImpl implements RestaurantSaveService {
  private final RestaurantSaveRepository restaurantSaveRepository;
  private final RestaurantMapper restaurantMapper;
  private final CustomerRepository customerRepository;
  /**
   * API lấy danh sách nhà hàng đã lưu có phân trang
   *
   * @param page  Trang hiện tại (bắt đầu từ 0).
   * @param size  Số lượng nhà hàng mỗi trang.
   * @return danh sachs nha hang
   */
  @Override
  public Page<RestaurantResponseDTO> getRestaurants(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();
    Pageable pageable = PageRequest.of(page, size);
    Page<RestaurantSave> results = restaurantSaveRepository.findByCustomer_Id(userId,pageable);
    return results.map(restaurantMapper::entitySave2RestaurantResponseDTO);
  }
}
