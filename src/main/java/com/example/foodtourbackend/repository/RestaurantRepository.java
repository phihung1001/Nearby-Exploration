package com.example.foodtourbackend.repository;

import com.example.foodtourbackend.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

        @Query("SELECT r FROM Restaurant r WHERE r.cityId IN :cityIds")
        List<Restaurant> findByCityIds(List<Long> cityIds);

        @Query("SELECT r FROM Restaurant r WHERE "
                + "(:cityIds IS NULL OR r.cityId IN :cityIds) "
                + "AND (:name IS NULL OR LOWER(r.name) LIKE CONCAT('%', LOWER(:name), '%'))")
        List<Restaurant> findByCityIdsAndName(
                @Param("cityIds") List<Long> cityIds,
                @Param("name") String name
        );

        @Query("SELECT r FROM Restaurant r WHERE "
                + "(6371 * acos(cos(radians(:latitude)) * cos(radians(r.latitude)) "
                + "* cos(radians(r.longitude) - radians(:longitude)) "
                + "+ sin(radians(:latitude)) * sin(radians(r.latitude)))) < :radius")
        List<Restaurant> findNearbyRestaurants(
                @Param("latitude") double latitude,
                @Param("longitude") double longitude,
                @Param("radius") double radius
        );

}
