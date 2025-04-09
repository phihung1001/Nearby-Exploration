package com.example.foodtourbackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDTO {

    private int age;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String email;
    private String address;
    private String gender;
}
