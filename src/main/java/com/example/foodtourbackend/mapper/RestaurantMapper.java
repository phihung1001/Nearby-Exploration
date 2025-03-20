package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.RestaurantDTO;
import com.example.foodtourbackend.entity.Restaurant;
import org.springframework.stereotype.Component;

//Mapper thủ công do mapstruct không genarate được ảo vl :)
@Component
public class RestaurantMapper {
    public static Restaurant mapperRestauranDtotToEntity(RestaurantDTO dto) {
        if (dto == null) {
            return null;
        }
        Restaurant entity = new Restaurant();
        entity.setId(dto.getId());
        entity.setCityId(dto.getCityId());
        entity.setDistrict(dto.getDistrict());
        entity.setName(dto.getName());
        entity.setAddress(dto.getAddress());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setLatitude(dto.getLatitude());
        entity.setLongitude(dto.getLongitude());
        entity.setTotalPictures(dto.getTotalPictures());
        entity.setAvgRatingText(dto.getAvgRatingText());
        entity.setTotalReviews(dto.getTotalReviews());
        entity.setPhotoUrl(dto.getPhotoUrl());
        return entity;
    }


    public static RestaurantDTO mapperEntityToRestaurantDTO(Restaurant entity) {
        if (entity == null) {
            return null;
        }
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(entity.getId());
        dto.setCityId(entity.getCityId());
        dto.setDistrict(entity.getDistrict());
        dto.setName(entity.getName());
        dto.setAddress(entity.getAddress());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setTotalPictures(entity.getTotalPictures());
        dto.setAvgRatingText(entity.getAvgRatingText());
        dto.setTotalReviews(entity.getTotalReviews());
        dto.setPhotoUrl(entity.getPhotoUrl());
        return dto;
    }
}

