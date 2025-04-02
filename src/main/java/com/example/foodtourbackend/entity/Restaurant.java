package com.example.foodtourbackend.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CITY_ID")
    private Long cityId;

    private String district;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Double latitude;
    private Double longitude;
    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;


    @Column(name = "TOTAL_PICTURES")
    private Integer totalPictures;

    @Column(name = "AVG_RATING")
    private String avgRatingText;

    @Column(name = "TOTAL_REVIEWS")
    private Long totalReviews;

    @Column(name = "PHOTO_URL")
    private String photoUrl;
    @Column(name = "district_id")
    private Integer districtId;

    // Constructor không tham số (cần cho JPA)
    public Restaurant() {
    }

    // Constructor đầy đủ tham số
    public Restaurant(Long id, Long cityId, String district, String name, String address, String phone, String email, Double latitude, Double longitude, String houseNumber, Integer totalPictures, String avgRatingText, Long totalReviews, String photoUrl, Integer districtId) {
        this.id = id;
        this.cityId = cityId;
        this.district = district;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.houseNumber = houseNumber;
        this.totalPictures = totalPictures;
        this.avgRatingText = avgRatingText;
        this.totalReviews = totalReviews;
        this.photoUrl = photoUrl;
      this.districtId = districtId;
    }

    // Getter và Setter cho id
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // Getter và Setter cho cityId
    public Long getCityId() {
        return cityId;
    }
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    // Getter và Setter cho district
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho address
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    // Getter và Setter cho phone
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    // Getter và Setter cho latitude
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    // Getter và Setter cho longitude
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    // Getter và Setter cho totalPictures
    public Integer getTotalPictures() {
        return totalPictures;
    }
    public void setTotalPictures(Integer totalPictures) {
        this.totalPictures = totalPictures;
    }

    // Getter và Setter cho avgRatingText
    public String getAvgRatingText() {
        return avgRatingText;
    }
    public void setAvgRatingText(String avgRatingText) {
        this.avgRatingText = avgRatingText;
    }

    // Getter và Setter cho totalReviews
    public Long getTotalReviews() {
        return totalReviews;
    }
    public void setTotalReviews(Long totalReviews) {
        this.totalReviews = totalReviews;
    }

    // Getter và Setter cho photoUrl
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    // equals() và hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
    public Integer getDistrictId() {
      return districtId;
    }
    public void setDistrictId(Integer districtId) {
      this.districtId = districtId;
    }

}
