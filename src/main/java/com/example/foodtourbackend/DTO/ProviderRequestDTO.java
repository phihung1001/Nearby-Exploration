package com.example.foodtourbackend.DTO;

import com.example.foodtourbackend.entity.CategoryFood;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProviderRequestDTO {
  private Integer userId;
  @NotBlank(message = "Tên nhà hàng không được để trống")
  private String name;
  @NotBlank(message = "Địa chỉ không được để trống")
  private String address;
  @NotBlank(message = "Số điện thoại không được để trống")
  private String phone;
  private String email;
  private String city;
  private Integer cityId;
  private String district;
  private Integer districtId;
  private String photoUrl;
  private String houseNumber;
  private List<CategoryFood> dishes;
}
