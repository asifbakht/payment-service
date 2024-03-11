package com.microservice.payment.mapper;

import com.microservice.payment.dto.CustomerDTO;
import com.microservice.payment.entity.Customer;
import org.mapstruct.Mapper;

/**
 * customer mapper class to avoid boilerplate code
 * this class transform dto to entity and vice versa
 *
 * @author Asif Bakht
 * @since 2024
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper {
    /**
     * converts dto to customer entity class
     *
     * @param customerDTO {@link CustomerDTO} customer object
     * @return {@link Customer} customer entity
     */
    Customer toEntity(final CustomerDTO customerDTO);

    /**
     * converts entity to dto class
     *
     * @param customer {@link Customer} customer entity
     * @return {@link CustomerDTO} customer dto class
     */
    CustomerDTO toDTO(final Customer customer);
}
