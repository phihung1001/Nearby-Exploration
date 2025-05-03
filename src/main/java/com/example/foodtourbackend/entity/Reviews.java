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
@Table(name ="reivews")
public class Reviews {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String comment;

  @Column(name = "AVG_RATING_TEXT")
  private Double avgRatingText;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_food_id")
  private CategoryFood categoryFood;
}
