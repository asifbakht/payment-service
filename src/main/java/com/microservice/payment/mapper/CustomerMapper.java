package com.microservice.payment.mapper;

import com.microservice.payment.dto.CustomerDTO;
import com.microservice.payment.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(final CustomerDTO customerDTO);
    CustomerDTO toDTO(final Customer customer);
}
