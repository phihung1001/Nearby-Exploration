package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.CustomerResponseDTO;
import com.example.foodtourbackend.DTO.UpdatePasswordRequestDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface CustomerService {
  CustomerResponseDTO getById(Long id);
  String searchByImage(MultipartFile file);
  CustomerResponseDTO update(Long id, CustomerRequestDTO customerRequestDTO);
  Map<String, Object> updatePassword(Long id, UpdatePasswordRequestDTO request);

  Map<String,String> upgrade();
}
