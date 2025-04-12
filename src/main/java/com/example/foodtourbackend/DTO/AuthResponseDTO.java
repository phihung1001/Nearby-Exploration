package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private CustomerRequestDTO customerRequestDTO;
}
