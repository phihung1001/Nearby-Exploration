package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.DishesDTO;
import com.example.foodtourbackend.entity.CategoryFood;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryFoodMapper {
   CategoryFoodMapper INSTANCE = Mappers.getMapper(CategoryFoodMapper.class);
   CategoryFood CategoryFoodToCategoryFood(CategoryFood categoryFood);
   CategoryFood DishesDTOToEntity(DishesDTO dishesDTO);
   CategoryFood UpdateDishesDTOToEntity(DishesDTO dto, @MappingTarget CategoryFood entity);
}
