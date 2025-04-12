package com.example.foodtourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant")
public class Restaurant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "CITY_ID")
  private Integer cityId;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private Customer customer;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CategoryFood> categoryFoods;

  @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Reviews> reviews;

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

}
