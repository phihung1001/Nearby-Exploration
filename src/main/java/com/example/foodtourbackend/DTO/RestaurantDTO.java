package com.example.foodtourbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDTO {
  private Long id;
  private Long cityId;
  private String district;
  private String name;
  private String address;
  private String phone;
  private String email;
  private Double latitude;
  private Double longitude;
  private Integer totalPictures;
  private String avgRatingText;
  private Long totalReviews;
  private String photoUrl;
}
