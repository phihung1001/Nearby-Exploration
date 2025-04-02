package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.service.RestaurantService;
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
public class RestaurantServiceImpl implements RestaurantService {

  private final RestaurantRepository restaurantRepository;

  /**
   * Constructor injection cho RestaurantRepository.
   *
   * @param restaurantRepository repository truy cập dữ liệu nhà hàng
   */
  public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

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
   * @param cityIds   Danh sách ID thành phố mà nhà hàng thuộc về.
   * @param districtIds Danh sách quận (districts) cần lọc.
   * @param name      Tên (hoặc một phần của tên) nhà hàng cần tìm.
   * @return Danh sách các nhà hàng thỏa mãn các tiêu chí lọc.
   */
  public List<Restaurant> filterRestaurants(List<Long> cityIds, List<Long> districtIds, String name) {
    // Lấy danh sách nhà hàng theo cityIds và tên nhà hàng từ repository
    List<Restaurant> results = restaurantRepository.findByCityIdsAndDistrictIdsAndName(cityIds,districtIds, name);
    return results;
  }

  /**
   * Lấy danh sách tất cả nhà hàng.
   *
   * @return Danh sách các nhà hàng.
   */
  @Override
  public List<Restaurant> findAll() {
    return restaurantRepository.findAll();
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
  public List<Restaurant> findNearbyRestaurants(double latitude, double longitude, double radius) {
    return restaurantRepository.findNearbyRestaurants(latitude, longitude, radius);
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
}
