package com.example.foodtourbackend.mapper;

import com.example.foodtourbackend.DTO.CustomerDTO;
import com.example.foodtourbackend.DTO.CustomerResponse;
import com.example.foodtourbackend.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    Customer DtoToEntity(CustomerDTO customerDTO);
    CustomerDTO EntityToDto(Customer customer);
    CustomerResponse entityToResponse(Customer customer);

    void updateCustomerFromDto(CustomerDTO customerDTO, @MappingTarget Customer customer);
}
