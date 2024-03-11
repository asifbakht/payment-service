package com.social.auth.mapper;

import com.social.auth.dto.CustomerDTO;
import com.social.auth.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(final CustomerDTO customerDTO);
    CustomerDTO toDTO(final Customer customer);
}
