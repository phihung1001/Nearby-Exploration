package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("public/customer")
public class CustomerController {
    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.getById(id));
    }

    @PostMapping("/searchByImage")
    public ResponseEntity<String> searchByImage(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body("File ảnh không hợp lệ");
        }
        return ResponseEntity.ok(customerService.searchByImage(file));
    }

}
