package com.example.foodtourbackend.service.serviceImpl;

import com.example.foodtourbackend.GlobalException.ErrorImportDataException;
import com.example.foodtourbackend.GlobalException.NotFoundException;
import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    @Value("${ai.service.url}")
    private String aiServiceUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ACCOUNT NOT FOUND"));
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

}
