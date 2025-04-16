package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.ProviderResponseDTO;
import com.example.foodtourbackend.DTO.RestaurantResponseDTO;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.ErrorImportDataException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.CategoryFood;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.entity.RestaurantSave;
import com.example.foodtourbackend.mapper.CategoryFoodMapper;
import com.example.foodtourbackend.mapper.RestaurantMapper;
import com.example.foodtourbackend.repository.CategoryFoodRepository;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.repository.RestaurantRepository;
import com.example.foodtourbackend.repository.RestaurantSaveRepository;
import com.example.foodtourbackend.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
  private final CustomerRepository customerRepository;
  private final RestaurantSaveRepository restaurantSaveRepository;
  /**
   * Lấy thông tin của một nhà hàng theo id.
   *
   * @param id ID của nhà hàng cần lấy thông tin.
   * @return Đối tượng Restaurant nếu tồn tại.
   * @throws NotFoundException nếu không tìm thấy nhà hàng với id được cung cấp.
   */
  @Override
  public RestaurantResponseDTO getById(Long id) {
    return  restaurantMapper.entity2RestaurantResponseDTO(restaurantRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Nhà hàng không tồn tại")));
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
  public Page<RestaurantResponseDTO> filterRestaurants(
    int page, int size,
    Integer cityId,
    Integer districtId,
    String name) {
    Pageable pageable = PageRequest.of(page, size);

    // Lấy danh sách nhà hàng theo cityIds và tên nhà hàng từ repository
    Page<Restaurant> results = restaurantRepository.findByCityIdAndDistrictIdAndName(cityId,districtId, name, pageable);
    return  results.map(restaurantMapper::entity2RestaurantResponseDTO);
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
  public Page<RestaurantResponseDTO> findNearbyRestaurants(
    int page, int size,
    double latitude,
    double longitude,
    double radius, String name) {
      Pageable pageable = PageRequest.of(page, size);
      Page<Restaurant> results = restaurantRepository.findNearbyRestaurants(latitude, longitude, radius, name, pageable);
      return results.map(restaurantMapper::entity2RestaurantResponseDTO);
  }


  /**
   *
   * Lấy danh sách nhà hàng theo phân trang.
   *
   * @param page  Trang hiện tại (bắt đầu từ 0).
   * @param size  Số lượng nhà hàng mỗi trang.
   * @return Một đối tượng Page chứa danh sách các nhà hàng theo phân trang.
   */
  public Page<RestaurantResponseDTO> getRestaurants(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Restaurant> results = restaurantRepository.findAll(pageable);
    return results.map(restaurantMapper::entity2RestaurantResponseDTO);
  }

  /**
   * Đăng ký nhà hàng mới cùng với danh sách món ăn (nếu có).
   *
   * @param requestDTO Thông tin đăng ký từ client (tên, địa chỉ, món ăn, v.v.)
   * @return ProviderResponseDTO chứa thông tin nhà hàng sau khi lưu thành công
   */
  @Override
  public ProviderResponseDTO registerRestaurant(ProviderRequestDTO requestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    Customer customer = customerRepository.findById(userId)
      .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

    // Kiểm tra email mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {
      Optional<Restaurant> existingByEmail = restaurantRepository.findByEmail(requestDTO.getEmail());
      if (existingByEmail.isPresent()) {
        throw new DuplicateException("Email đã được đăng ký cho một nhà hàng khác.");
      }
    }

    // Kiểm tra số điện thoại mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    if (requestDTO.getPhone() != null && !requestDTO.getPhone().isEmpty()) {
      Optional<Restaurant> existingByPhone = restaurantRepository.findByPhone(requestDTO.getPhone());
      if (existingByPhone.isPresent()) {
        throw new DuplicateException("Số điện thoại đã được đăng ký cho một khách hàng khác.");
      }
    }
    Restaurant restaurant = restaurantMapper.ProviderRequestDTOToEntity(requestDTO);
    restaurant.setCustomer(customer);

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    if (requestDTO.getDishes() != null && !requestDTO.getDishes().isEmpty()) {
      List<CategoryFood> dishList = requestDTO.getDishes().stream()
        .map(categoryFoodMapper::CategoryFoodToCategoryFood)
        .peek(dish -> dish.setRestaurant(savedRestaurant)) // Gán nhà hàng cho từng món
        .toList();
      categoryFoodRepository.saveAll(dishList);
    }
    return restaurantMapper.EntityToProviderResponseDTO(savedRestaurant);
  }

  /**
   * Cập nhật thông tin nhà hàng mới
   *
   * @param requestDTO Thông tin đăng ký từ client (tên, địa chỉ, món ăn, v.v.)
   * @param id id nhà hàng
   * @return ProviderResponseDTO chứa thông tin nhà hàng sau khi lưu thành công
   */
  @Override
  public ProviderResponseDTO updateRestaurant(ProviderRequestDTO requestDTO, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    Optional<Restaurant> restaurant = restaurantRepository.findByIdAndCustomer_Id(id, userId);
    if(restaurant.isEmpty()) {
      throw new UnauthorizedException("Nhà hàng không tồn tại hoặc không thuộc quyền sở hữu.");
    }

    // Kiểm tra email mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {
      Optional<Restaurant> existingByEmail = restaurantRepository.findByEmail(requestDTO.getEmail());
      if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(restaurant.get().getId())) {
        throw new DuplicateException("Email đã được đăng ký cho một nhà hàng khác.");
      }
    }


    // Kiểm tra số điện thoại mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    if (requestDTO.getPhone() != null && !requestDTO.getPhone().isEmpty()) {
      Optional<Restaurant> existingByPhone = restaurantRepository.findByPhone(requestDTO.getPhone());
      if (existingByPhone.isPresent() && !existingByPhone.get().getId().equals(restaurant.get().getId())) {
        throw new DuplicateException("Số điện thoại đã được đăng ký cho một khách hàng khác.");
      }
    }
    restaurantMapper.UpdateProviderRequestDTOToEntity(requestDTO, restaurant.get());
    return restaurantMapper.EntityToProviderResponseDTO(restaurantRepository.save(restaurant.get()));
  }

  /**
   * Lấy danh sách nhà hàng mà user_id đang quản lí
   *
   * @return danh sách nhà hàng mà người dùng đang quản lí
   */
  @Override
  public Page<ProviderResponseDTO> getList(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    Pageable pageable = PageRequest.of(page, size);
    Page<Restaurant> restaurants = restaurantRepository.findByCustomer_Id(userId, pageable);

    if (restaurants.isEmpty()) {
      throw new NotFoundException("Bạn chưa quản lý nhà hàng nào cả.");
    }

    return restaurants.map(restaurantMapper::EntityToProviderResponseDTO);
  }

  /**
   * Xóa thông tin nhà hàng
   *
   * @param id
   * @return
   */
  @Override
  public String delete(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    Restaurant restaurant = restaurantRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Không tồn tại bản ghi món ăn trong database"));
    if (! restaurant.getCustomer().getId().equals(userId)) {
      throw new UnauthorizedException("Bạn không có quyền xóa nhà hàng này");
    }
    restaurantRepository.delete(restaurant);
    return "Xóa nhà hàng thành công";
  }

  /**
   * Lưu thông tin nhà hàng yêu thích
   *
   * @param id nhà hàng cần luu.
   * @return Restaurant thông tin nhà hàng đã lưu.
   */
  @Override
  public ResponseEntity<?> save(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Collections.singletonMap("message", "Vui lòng đăng nhập để lưu nhà hàng"));
      }
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();
    Optional<Customer> customer = customerRepository.findById(userId);
    Optional<Restaurant> restaurant = restaurantRepository.findById(id);
    if (restaurant.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Collections.singletonMap("message", "Nhà hàng không tồn tại"));
    }
    Optional<RestaurantSave> restaurantSave = restaurantSaveRepository.findByCustomer_idAndRestaurant_id(userId,id);
    if (restaurantSave.isPresent()) {
      throw new ErrorImportDataException("Nhà hàng đã lưu trước đó");
    }
    RestaurantSave restaurantSaveEntity = new RestaurantSave();
    restaurantSaveEntity.setCustomer(customer.get());
    restaurantSaveEntity.setRestaurant(restaurant.get());
    restaurantSaveRepository.save(restaurantSaveEntity);
    return ResponseEntity.ok(Collections.singletonMap("message", "Lưu thành công"));
  }

}
