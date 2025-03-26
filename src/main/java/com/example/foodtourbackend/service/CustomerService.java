package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.CustomerDTO;
import com.example.foodtourbackend.DTO.CustomerResponse;
import com.example.foodtourbackend.DTO.UpdatePasswordRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface CustomerService {
    CustomerResponse getById(Long id);
    String searchByImage(MultipartFile file);

  CustomerResponse update(Long id,CustomerDTO customerDTO);

  Map<String, Object> updatePassword(Long id, UpdatePasswordRequest request);
}
