package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.ProviderResponseDTO;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.entity.CategoryFood;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.mapper.RestaurantMapper;
import com.example.foodtourbackend.repository.CategoryFoodRepository;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation của RestaurantService.
 * Lớp này cung cấp các phương thức xử lý logic cho các thao tác liên quan đến nhà hàng,
 * bao gồm: lấy thông tin theo id, lọc theo tiêu chí, tìm tất cả, tìm nhà hàng gần đó,
 * cũng như phân trang danh sách nhà hàng.
 */
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final RestaurantMapper restaurantMapper;
  private final CategoryFoodMapper categoryFoodMapper;
  private final CategoryFoodRepository categoryFoodRepository;

  /**
   * Lấy thông tin của một nhà hàng theo id.
   *
   * @param id ID của nhà hàng cần lấy thông tin.
   * @return Đối tượng Restaurant nếu tồn tại.
   * @throws NotFoundException nếu không tìm thấy nhà hàng với id được cung cấp.
   */
  @Override
  public Restaurant getById(Long id) {
    return restaurantRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Nhà hàng không tồn tại"));
  }

  /**
   * Lọc danh sách nhà hàng dựa trên các tiêu chí:
   * - Danh sách cityIds
   * - Danh sách districts (quận)
   * - Tên nhà hàng (name)
   *
   * @param cityId  Danh sách ID thành phố mà nhà hàng thuộc về.
   * @param districtId Danh sách quận (districts) cần lọc.
   * @param name      Tên (hoặc một phần của tên) nhà hàng cần tìm.
   * @return Danh sách các nhà hàng thỏa mãn các tiêu chí lọc và phân trang.
   */
  public Page<Restaurant> filterRestaurants(
    int page, int size,
    Integer cityId,
    Integer districtId,
    String name) {
    Pageable pageable = PageRequest.of(page, size);

    // Lấy danh sách nhà hàng theo cityIds và tên nhà hàng từ repository
    Page<Restaurant> results = restaurantRepository.findByCityIdAndDistrictIdAndName(cityId,districtId, name, pageable);
    return results;
  }

  /**
   * Tìm danh sách các nhà hàng gần một vị trí địa lý cụ thể.
   *
   * @param latitude  Vĩ độ của vị trí cần tìm nhà hàng gần đó.
   * @param longitude Kinh độ của vị trí cần tìm nhà hàng gần đó.
   * @param radius    Bán kính (theo đơn vị phù hợp, ví dụ: km) để tìm kiếm.
   * @return Danh sách các nhà hàng nằm trong bán kính được chỉ định.
   */
  @Override
  public Page<Restaurant> findNearbyRestaurants(
    int page, int size,
    double latitude,
    double longitude,
    double radius, String name) {
      Pageable pageable = PageRequest.of(page, size);
      return restaurantRepository.findNearbyRestaurants(latitude, longitude, radius, name, pageable);
  }


  /**
   * Lấy danh sách nhà hàng theo phân trang.
   *
   * @param page  Trang hiện tại (bắt đầu từ 0).
   * @param size  Số lượng nhà hàng mỗi trang.
   * @return Một đối tượng Page chứa danh sách các nhà hàng theo phân trang.
   */
  public Page<Restaurant> getRestaurants(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    return restaurantRepository.findAll(pageable);
  }

  /**
   * Đăng ký nhà hàng mới cùng với danh sách món ăn (nếu có).
   *
   * @param requestDTO Thông tin đăng ký từ client (tên, địa chỉ, món ăn, v.v.)
   * @return ProviderResponseDTO chứa thông tin nhà hàng sau khi lưu thành công
   */
  @Override
  public ProviderResponseDTO registerRestaurant(ProviderRequestDTO requestDTO) {
    Restaurant restaurant = restaurantMapper.ProviderRequestDTOToEntity(requestDTO);
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    if (requestDTO.getDishes() != null && !requestDTO.getDishes().isEmpty()) {
      List<CategoryFood> dishList = requestDTO.getDishes().stream().map(
        categoryFoodMapper::CategoryFoodToCategoryFood
      ).toList();
      categoryFoodRepository.saveAll(dishList);
    }
    return restaurantMapper.EntityToProviderResponseDTO(savedRestaurant);
  }

  @Override
  public Object updateRestaurant(ProviderRequestDTO requestDTO, Long id) {
    return null;
  }
}
