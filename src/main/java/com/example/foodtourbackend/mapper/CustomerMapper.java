package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.request.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.request.RegisterRequestDTO;
import com.example.foodtourbackend.DTO.response.CustomerResponseDTO;
import com.example.foodtourbackend.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    Customer DtoToEntity(CustomerRequestDTO customerRequestDTO);
    CustomerRequestDTO EntityToDto(Customer customer);
    CustomerResponseDTO entityToResponse(Customer customer);
    Customer RegisterRequestDTO2Entity(RegisterRequestDTO requestDTO);
    void updateCustomerFromDto(CustomerRequestDTO customerRequestDTO, @MappingTarget Customer customer);
}
