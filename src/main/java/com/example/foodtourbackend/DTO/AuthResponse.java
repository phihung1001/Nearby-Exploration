package com.example.foodtourbackend.DTO;

public class AuthResponse {
    private String token;
    private CustomerDTO customerDTO;
    public AuthResponse(String token, CustomerDTO customerDTO) {
        this.token = token;
        this.customerDTO = customerDTO;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public CustomerDTO getCustomerDTO() {
        return customerDTO;
    }
    public void setCustomerDTO(CustomerDTO customerDTO) {
        this.customerDTO = customerDTO;
    }
}
