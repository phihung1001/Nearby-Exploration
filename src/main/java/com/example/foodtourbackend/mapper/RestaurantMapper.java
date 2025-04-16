package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.ProviderRequestDTO;
import com.example.foodtourbackend.DTO.ProviderResponseDTO;
import com.example.foodtourbackend.DTO.RestaurantResponseDTO;
import com.example.foodtourbackend.entity.Restaurant;
import com.example.foodtourbackend.entity.RestaurantSave;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RestaurantMapper {
  Restaurant ProviderRequestDTOToEntity(ProviderRequestDTO providerRequestDTO);
  ProviderResponseDTO EntityToProviderResponseDTO(Restaurant restaurant);
  void UpdateProviderRequestDTOToEntity(ProviderRequestDTO providerRequestDTO, @MappingTarget Restaurant restaurant);
  RestaurantResponseDTO entity2RestaurantResponseDTO(Restaurant restaurant);

  /**
   * Ánh xạ từ RestaurantSave sang RestaurantResponseDTO
   * Chỉ lấy thông tin từ Restaurant, bỏ qua thông tin của RestaurantSave
   *
   * @param restaurantSave đối tượng RestaurantSave
   * @return đối tượng RestaurantResponseDTO
   */
  @Mapping(source = "restaurant.id", target = "id")
  @Mapping(source = "restaurant.name", target = "name")
  @Mapping(source = "restaurant.address", target = "address")
  @Mapping(source = "restaurant.phone", target = "phone")
  @Mapping(source = "restaurant.email", target = "email")
  @Mapping(source = "restaurant.latitude", target = "latitude")
  @Mapping(source = "restaurant.longitude", target = "longitude")
  @Mapping(source = "restaurant.houseNumber", target = "houseNumber")
  @Mapping(source = "restaurant.totalPictures", target = "totalPictures")
  @Mapping(source = "restaurant.avgRatingText", target = "avgRatingText")
  @Mapping(source = "restaurant.totalReviews", target = "totalReviews")
  @Mapping(source = "restaurant.photoUrl", target = "photoUrl")
  RestaurantResponseDTO entitySave2RestaurantResponseDTO(RestaurantSave restaurantSave);
}

