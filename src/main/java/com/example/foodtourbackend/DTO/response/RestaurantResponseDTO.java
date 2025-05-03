package com.example.foodtourbackend.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantResponseDTO {
  private Long id;
  private Integer cityId;
  private String district;
  private String name;
  private String address;
  private String phone;
  private String email;
  private Double latitude;
  private Double longitude;
  private String houseNumber;
  private Integer totalPictures;
  private String avgRatingText;
  private Long totalReviews;
  private String photoUrl;
  private Integer districtId;
}
