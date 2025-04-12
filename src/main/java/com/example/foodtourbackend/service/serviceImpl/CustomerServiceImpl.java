package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.CustomerResponseDTO;
import com.example.foodtourbackend.DTO.UpdatePasswordRequestDTO;
import com.example.foodtourbackend.GlobalException.DuplicateException;
import com.example.foodtourbackend.GlobalException.ErrorImportDataException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.GlobalException.UnauthorizedException;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.mapper.CustomerMapper;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  @Value("${ai.service.url}")
  private String aiServiceUrl;
  private final RestTemplate restTemplate = new RestTemplate();
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  /**
   * Lấy thông tin khách hàng dựa trên ID.
   * Kiểm tra quyền truy cập: chỉ cho phép người dùng lấy đúng thông tin của chính họ.
   *
   * @param id ID người dùng cần truy vấn
   * @return Thông tin khách hàng dạng DTO
   * @throws UnauthorizedException nếu người dùng cố truy cập hồ sơ của người khác
   * @throws NotFoundException nếu không tìm thấy người dùng
   */
  @Override
  public CustomerResponseDTO getById(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();;
    Long userId = userDetails.getUserId();

    if (!userId.equals(id)) {
        throw new UnauthorizedException("Bạn không có quyền truy cập hồ sơ này.");
    }
    Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isPresent()) {
      return customerMapper.entityToResponse(customer.get());
    }
    throw new NotFoundException("Tài khoản không tồn tại");
  }

  /**
   * Gửi ảnh lên AI Service để xử lý nhận diện hoặc tìm kiếm.
   *
   * @param file File ảnh cần gửi
   * @return Kết quả trả về từ AI Service
   * @throws ErrorImportDataException nếu có lỗi trong quá trình gửi dữ liệu
   */
  @Override
  public String searchByImage(MultipartFile file) {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);

      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

      body.add("file", new ByteArrayResource(file.getBytes()) {
          @Override
          public String getFilename() {
              return file.getOriginalFilename(); // Cần trả về tên file để AI Service nhận diện
          }
      });
      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      ResponseEntity<String> response = restTemplate.postForEntity(aiServiceUrl, requestEntity, String.class);

      return response.getBody();
    } catch (Exception e) {
      throw new ErrorImportDataException("Gửi file tới AI Service thất bại: " + e.getMessage());
    }
  }

  /**
   * Cập nhật thông tin người dùng.
   * Xác thực người dùng từ token và kiểm tra tính hợp lệ của dữ liệu trước khi lưu.
   *
   * @param id ID người dùng cần cập nhật
   * @param customerRequestDTO Dữ liệu thông tin người dùng mới
   * @return Thông tin khách hàng sau khi cập nhật
   * @throws DuplicateException nếu email hoặc số điện thoại đã được đăng ký bởi người khác
   * @throws IllegalArgumentException nếu dữ liệu nhập vào không hợp lệ
   * @throws NotFoundException nếu người dùng không tồn tại
   */
  @Override
  public CustomerResponseDTO update(Long id, CustomerRequestDTO customerRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    if (!userId.equals(id)) {
      throw new UnauthorizedException("Bạn không có quyền chỉnh sửa thông tin người dùng này");
    }

    Customer customer = customerRepository.findById(userId)
      .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

    // Kiểm tra email mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    Customer existingByEmail = customerRepository.findByEmail(customerRequestDTO.getEmail())
      .orElse(null);

    if (existingByEmail != null && !existingByEmail.getId().equals(customer.getId())) {
        throw new DuplicateException("Email đã được đăng ký cho một khách hàng khác.");
    }

    // Kiểm tra số điện thoại mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    Customer existingByPhone = customerRepository.findByPhoneNumber(customerRequestDTO.getPhoneNumber()).orElse(null);
    if (existingByPhone != null && !existingByPhone.getId().equals(customer.getId())) {
        throw new DuplicateException("Số điện thoại đã được đăng ký cho một khách hàng khác.");
    }

    //Kiểm tra tuổi hợp lệ
    if(customerRequestDTO.getAge() < 0 || customerRequestDTO.getAge() > 100) {
      throw new IllegalArgumentException("Độ tuổi không phù hợp");
    }
    if (!customerRequestDTO.getPhoneNumber().matches("^(\\+84|0)[3|5|7|8|9][0-9]{8}$")) {
      throw new IllegalArgumentException("Số điện thoại không đúng định dạng ");
    }

    customerMapper.updateCustomerFromDto(customerRequestDTO, customer);
    if (customerRequestDTO.getPassword() != null && !customerRequestDTO.getPassword().isEmpty()) {
        customer.setPassword(passwordEncoder.encode(customerRequestDTO.getPassword()));
    }
    customerRepository.save(customer);
    return customerMapper.entityToResponse(customer);
  }

  /**
   * Đổi mật khẩu cho người dùng.
   * Kiểm tra người dùng hợp lệ, xác nhận mật khẩu cũ và đảm bảo mật khẩu mới khác mật khẩu hiện tại.
   *
   * @param id ID người dùng cần đổi mật khẩu
   * @param request Thông tin mật khẩu cũ và mới
   * @return Thông báo thành công
   * @throws IllegalArgumentException nếu mật khẩu cũ sai hoặc mật khẩu mới không hợp lệ
   * @throws NotFoundException nếu không tìm thấy người dùng
   */
  @Override
  public Map<String, Object> updatePassword(Long id, UpdatePasswordRequestDTO request) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new UnauthorizedException("Chưa đăng nhập hoặc token không hợp lệ");
    }
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    Long userId = userDetails.getUserId();

    if (!userId.equals(id)) {
      throw new UnauthorizedException("Bạn không có quyền chỉnh sửa thông tin người dùng này");
    }

    Customer customer = customerRepository.findById(userId)
      .orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng"));

    if (!passwordEncoder.matches(request.getOldPassword(), customer.getPassword())) {
      throw new IllegalArgumentException("Mật khẩu cũ không đúng");
    }

    if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
      throw new IllegalArgumentException("Mật khẩu mới không được để trống");
    }

    if (passwordEncoder.matches(request.getNewPassword(), customer.getPassword())) {
      throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu cũ");
    }

    customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
    customerRepository.save(customer);

    return Map.of("message", "Đổi mật khẩu thành công");
  }

}
