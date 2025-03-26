package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.RestaurantDTO;
import com.example.foodtourbackend.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantMapper {
    Restaurant DtoToEntity(RestaurantDTO restaurantDTO);

    RestaurantDTO EntityToDTO(Restaurant restaurant);
}

