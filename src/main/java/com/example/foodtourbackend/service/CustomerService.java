package com.example.foodtourbackend.service;

import com.example.foodtourbackend.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CustomerService {
    Customer getById(Long id);
    String searchByImage(MultipartFile file);
}
