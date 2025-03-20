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
import java.util.stream.Collectors;
@Service
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RESTAURANT NOT FOUND"));
    }

    public List<Restaurant> filterRestaurants(List<Long> cityIds, List<String> districts, String name) {
        List<Restaurant> results = restaurantRepository.findByCityIdsAndName(cityIds, name);

        // Lọc thêm theo districts (nếu có)
        if (districts != null && !districts.isEmpty()) {
            results = results.stream()
                    .filter(r -> districts.stream()
                            .anyMatch(d -> r.getDistrict().toLowerCase().replace("quận", "").trim()
                                    .contains(d.toLowerCase().trim())))
                    .collect(Collectors.toList());
        }

        return results;
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> findNearbyRestaurants(double latitude, double longitude, double radius) {
        return restaurantRepository.findNearbyRestaurants(latitude, longitude, radius);
    }

    /**
     * Lấy danh sách nhà hàng có phân trang
     *
     * @param page Trang hiện tại (bắt đầu từ 0)
     * @param size Số lượng nhà hàng mỗi trang
     * @return Trang chứa danh sách nhà hàng
     */
    public Page<Restaurant> getRestaurants(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return restaurantRepository.findAll(pageable);
    }

}




