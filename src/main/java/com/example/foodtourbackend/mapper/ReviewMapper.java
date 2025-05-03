package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.request.CommentDTO;
import com.example.foodtourbackend.DTO.response.CommentResponseDTO;
import com.example.foodtourbackend.entity.Reviews;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewMapper {
  Reviews commentDTO2Reviews(CommentDTO commentDTO);
  CommentResponseDTO reviews2CommentResponseDTO(Reviews reviews);
}
