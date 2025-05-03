package com.example.foodtourbackend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenResponseDTO {
    private String accessToken;
}
