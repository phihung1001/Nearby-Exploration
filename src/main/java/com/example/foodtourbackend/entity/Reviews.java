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

@Table(name ="reivews")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RESTAURANT_ID")
    private Long restaurantId;
    @Column(name = "USER_ID")
    private Long userId;
    private String comment;

    @Column(name = "AVG_RATING_TEXT")
    private String avgRatingText;
}
