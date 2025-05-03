package com.example.foodtourbackend.service;

import com.example.foodtourbackend.DTO.request.CommentDTO;
import com.example.foodtourbackend.DTO.request.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.request.UpdatePasswordRequestDTO;
import com.example.foodtourbackend.DTO.response.ApiResponse;
import com.example.foodtourbackend.DTO.response.CustomerResponseDTO;
import org.springframework.http.ResponseEntity;
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

  ResponseEntity<ApiResponse<CommentDTO>> comment(CommentDTO commentDTO);
}
