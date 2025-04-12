package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.CustomerResponseDTO;
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

    void updateCustomerFromDto(CustomerRequestDTO customerRequestDTO, @MappingTarget Customer customer);
}
