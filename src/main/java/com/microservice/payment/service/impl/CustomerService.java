package com.microservice.payment.service.impl;

import com.microservice.payment.dto.CustomerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * customer related business logic resides here
 *
 * @author Asif Bakht
 * @since 2024
 */
public interface CustomerService {

    public CustomerDTO add(final CustomerDTO customerDTO);

    public CustomerDTO update(final String id, final CustomerDTO customerDTO);

    public CustomerDTO get(final String id);

    public void delete(final String id);

    public Page<CustomerDTO> getAll(final Pageable pageable, final String id);
}
