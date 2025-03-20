package com.example.foodtourbackend.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// khong dung duoc lombok nen phai contruct thu cong
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

    public RestaurantDTO() {}

    public RestaurantDTO(Long id, Long cityId, String district, String name, String address, String phone, String email, Double latitude, Double longitude, Integer totalPictures, String avgRatingText, Long totalReviews, String photoUrl) {
        this.id = id;
        this.cityId = cityId;
        this.district = district;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalPictures = totalPictures;
        this.avgRatingText = avgRatingText;
        this.totalReviews = totalReviews;
        this.photoUrl = photoUrl;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getCityId() {
        return cityId;
    }
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalPictures() {
        return totalPictures;
    }
    public void setTotalPictures(Integer totalPictures) {
        this.totalPictures = totalPictures;
    }

    public String getAvgRatingText() {
        return avgRatingText;
    }
    public void setAvgRatingText(String avgRatingText) {
        this.avgRatingText = avgRatingText;
    }

    public Long getTotalReviews() {
        return totalReviews;
    }
    public void setTotalReviews(Long totalReviews) {

        this.totalReviews = totalReviews;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

