package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
}
