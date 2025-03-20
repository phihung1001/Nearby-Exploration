package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.service.RestaurantService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Lấy thông tin nhà hàng theo ID.
     *
     * @param id ID nhà hàng
     * @return ResponseEntity chứa thông tin nhà hàng
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Restaurant restaurant = restaurantService.getById(id);
            if (restaurant == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Restaurant not found");
            }
            return ResponseEntity.ok(restaurant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching the restaurant");
        }
    }

    /**
     * Lọc danh sách nhà hàng theo cityIds, districts, hoặc name.
     *
     * @param cityIds Danh sách ID thành phố
     * @param districts Danh sách quận/huyện
     * @param name Tên nhà hàng
     * @return ResponseEntity chứa danh sách nhà hàng lọc được
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Restaurant>> filterRestaurants(
            @RequestParam(required = false) List<Long> cityIds,
            @RequestParam(required = false) List<String> districts,
            @RequestParam(required = false) String name) {
        try {
            if ((cityIds == null || cityIds.isEmpty())
                    && (districts == null || districts.isEmpty())
                    && (name == null || name.isEmpty())) {
                return ResponseEntity.ok(restaurantService.findAll());
            }
            return ResponseEntity.ok(restaurantService.filterRestaurants(cityIds, districts, name));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * Tìm kiếm nhà hàng gần vị trí hiện tại.
     *
     * @param latitude Vĩ độ
     * @param longitude Kinh độ
     * @param radius Bán kính tìm kiếm (mặc định 5km)
     * @return ResponseEntity chứa danh sách nhà hàng gần vị trí
     */
    @GetMapping("/nearby")
    public ResponseEntity<List<Restaurant>> getNearbyRestaurants(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") double radius) {
        try {
            if (radius <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Kiểm tra tham số không hợp lệ
            }
            return ResponseEntity.ok(restaurantService.findNearbyRestaurants(latitude, longitude, radius));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
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
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<Restaurant> restaurants = restaurantService.getRestaurants(page, size);
        if (restaurants.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(restaurants);
    }

}
