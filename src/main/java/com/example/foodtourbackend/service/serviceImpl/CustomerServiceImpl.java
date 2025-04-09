package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.DTO.CustomerDTO;
import com.example.foodtourbackend.DTO.CustomerResponse;
import com.example.foodtourbackend.DTO.UpdatePasswordRequest;
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

  @Override
  public CustomerResponse getById(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();;
    Long userId = userDetails.getUserId();

    System.out.println("userID "+ userId);
    if (!userId.equals(id)) {
        throw new UnauthorizedException("Bạn không có quyền truy cập hồ sơ này.");
    }
    Optional<Customer> customer = customerRepository.findById(id);
    if (customer.isPresent()) {
      return customerMapper.entityToResponse(customer.get());
    }
    throw new NotFoundException("ACCOUNT NOT FOUND");
  }

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

  @Override
  public CustomerResponse update(Long id,CustomerDTO customerDTO) {
    Customer customer = customerRepository.findById(id).orElseThrow(
      () -> new NotFoundException("Không tìm thấy khách hàng với id: " + id));

    // Kiểm tra email mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    Customer existingByEmail = customerRepository.findByEmail(customerDTO.getEmail())
      .orElse(null);

    if (existingByEmail != null && !existingByEmail.getId().equals(customer.getId())) {
        throw new DuplicateException("Email đã được đăng ký cho một khách hàng khác.");
    }

    // Kiểm tra số điện thoại mới có trùng với bản ghi nào khác không (ngoại trừ chính bản ghi hiện tại)
    Customer existingByPhone = customerRepository.findByPhoneNumber(customerDTO.getPhoneNumber()).orElse(null);
    if (existingByPhone != null && !existingByPhone.getId().equals(customer.getId())) {
        throw new DuplicateException("Số điện thoại đã được đăng ký cho một khách hàng khác.");
    }

    //Kiểm tra tuổi hợp lệ
    if(customerDTO.getAge() < 0 || customerDTO.getAge() > 100) {
      throw new IllegalArgumentException("Độ tuổi không phù hợp");
    }
    if (!customerDTO.getPhoneNumber().matches("^(\\+84|0)[3|5|7|8|9][0-9]{8}$")) {
      throw new IllegalArgumentException("Số điện thoại không đúng định dạng ");
    }
    customerMapper.updateCustomerFromDto(customerDTO, customer);
    if (customerDTO.getPassword() != null && !customerDTO.getPassword().isEmpty()) {
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
    }
    customerRepository.save(customer);
    return customerMapper.entityToResponse(customer);
  }

  @Override
  public Map<String, Object> updatePassword(Long id, UpdatePasswordRequest request) {
    Customer customer = customerRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Không tìm thấy khách hàng với id: " + id));

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
