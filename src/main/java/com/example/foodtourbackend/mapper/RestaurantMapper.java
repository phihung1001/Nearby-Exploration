package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.ProviderResponseDTO;
import com.example.foodtourbackend.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantMapper {
    Restaurant ProviderRequestDTOToEntity(ProviderRequestDTO providerRequestDTO);
    ProviderResponseDTO EntityToProviderResponseDTO(Restaurant restaurant);
}

