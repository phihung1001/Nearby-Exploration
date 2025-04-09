package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

  private final RestaurantService restaurantService;

    /**
     * Lấy thông tin nhà hàng theo ID.
     *
     * @param id ID nhà hàng
     * @return ResponseEntity chứa thông tin nhà hàng
     */
  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getById(id));
  }

    /**
     * Lọc danh sách nhà hàng theo cityIds, districts, hoặc name có phân trang.
     *
     * @param page Trang hiện tại (mặc định là 0)
     * @param size Số lượng nhà hàng mỗi trang (mặc định là 20)
     * @param cityId Danh sách ID thành phố
     * @param districtId Danh sách quận/huyện
     * @param name Tên nhà hàng
     * @return ResponseEntity chứa danh sách nhà hàng lọc được theo phân trang
     */
  @GetMapping("/filter")
  public ResponseEntity<Page<Restaurant>> filterRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(required = false) Integer cityId,
    @RequestParam(required = false) Integer districtId,
    @RequestParam(required = false) String name) {
      if (cityId == null
        && districtId == null
        && name == null ) {
          return ResponseEntity.ok(restaurantService.getRestaurants(page,size));
      }
      return ResponseEntity.ok(restaurantService.filterRestaurants(page, size,cityId, districtId, name));
    }

    /**
     * Api Tìm kiếm nhà hàng gần vị trí hiện tại có phân trang
     *
     * @param page Trang hiện tại (mặc định là 0)
     * @param size Số lượng nhà hàng mỗi trang (mặc định là 20)
     * @param latitude Vĩ độ
     * @param longitude Kinh độ
     * @param radius Bán kính tìm kiếm (mặc định 5km)
     * @param name tên nhà hàng tìm kiếm
     * @return ResponseEntity chứa danh sách nhà hàng gần vị trí theo phân trang
     */
  @GetMapping("/nearby")
  public ResponseEntity<Page<Restaurant>> getNearbyRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam double latitude,
    @RequestParam double longitude,
    @RequestParam(defaultValue = "15") double radius,
    @RequestParam(required = false) String name) {
      return ResponseEntity.ok(restaurantService.findNearbyRestaurants(page, size, latitude, longitude, radius, name));
    }

    /**
     * API lấy danh sách nhà hàng có phân trang
     *
     * @param page Trang hiện tại (mặc định là 0)
     * @param size Số lượng nhà hàng mỗi trang (mặc định là 20)
     * @return Danh sách nhà hàng theo phân trang
     */
  @GetMapping("/list")
  public ResponseEntity<Page<Restaurant>> getRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
      return ResponseEntity.ok(restaurantService.getRestaurants(page, size));
    }

}
