package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.ProviderRequestDTO;
import com.example.foodtourbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
   * Lấy danh sách nhà hàng theo Id người dùng
   *
   * Param id Id người dùng
   * @return Danh sách nhà hàng mà nguời dùng đang quản lí
   */
  @GetMapping("list-restaurant/user")
  public ResponseEntity<?> getList(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getList(page,size));
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
  public ResponseEntity<Page<?>> filterRestaurants(
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
  public ResponseEntity<Page<?>> getNearbyRestaurants(
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
  public ResponseEntity<Page<?>> getRestaurants(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size) {
      return ResponseEntity.ok(restaurantService.getRestaurants(page, size));
    }

  /**
   * Đăng kí nhà hàng và danh sách món ăn
   *
   * @param requestDTO Dữ liệu đăng kí nhà hàng ( tên , địa chỉ, menu món ăn ,.. )
   * @return Trả về thông báo đăng kí thành công hay thất bại
   */

  @PreAuthorize("hasAuthority('PROVIDER')")
  @PostMapping("/register")
  public ResponseEntity<?> registerRestaurant(@RequestBody ProviderRequestDTO requestDTO) {
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.registerRestaurant(requestDTO));
  }

  /**
   * Chỉnh sửa thông tin nhà hàng
   *
   * @param requestDTO Dữ liệu đăng kí nhà hàng ( tên , địa chỉ, menu món ăn ,.. )
   * @return Trả về thông báo đăng kí thành công hay thất bại
   */

  @PreAuthorize("hasAuthority('PROVIDER')")
  @PostMapping("/update/{id}")
  public ResponseEntity<?> updateRestaurant(
    @RequestBody ProviderRequestDTO requestDTO,
    @PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateRestaurant(requestDTO, id));
  }

  /**
   * Xóa thông tin nhà hàng
   *
   */
  @PreAuthorize("hasAuthority('PROVIDER')")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
    return ResponseEntity.ok(restaurantService.delete(id));
  }

  /**
   * Lưu nhà hàng yêu thích
   *
   * @param id nhà hàng cần lưu
   * @return Restaurant thông tin nhà hàng đã lưu
   */
  @PostMapping("/save/{id}")
  @PreAuthorize("hasAnyAuthority('CUSTOMER', 'PROVIDER')")
  public ResponseEntity<?> save(@PathVariable Long id) {
    return ResponseEntity.ok(restaurantService.save(id));
  }

}
