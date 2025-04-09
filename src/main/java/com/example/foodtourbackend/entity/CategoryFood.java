package com.example.foodtourbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_food")
public class CategoryFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "CATEGORY_NAME")
    private String name;

    private String description;
    private String image;
    private String price;

    @Column(name="RESTAURANT_ID")
    private Long restaurantId;

    @Column(name = "CITY_ID")
    private Long cityId;
    @Column(name = "DISTRICT_ID")
    private Long districtId;
}
