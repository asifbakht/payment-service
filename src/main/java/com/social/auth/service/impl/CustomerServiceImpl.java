package com.social.auth.service.impl;

import com.social.auth.dto.CustomerDTO;
import com.social.auth.entity.Customer;
import com.social.auth.exception.NotFoundException;
import com.social.auth.mapper.CustomerMapper;
import com.social.auth.repository.CustomerRepository;
import com.social.auth.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private static final String NOT_FOUND = "customer not found";
    private static final String REQUIRE_ID = "id cannot be null";

    @Override
    public CustomerDTO add(CustomerDTO customerDTO) {
        final Customer customer = customerRepository
                .findByEmail(customerDTO.email())
                .orElseGet(() -> customerRepository
                        .save(customerMapper.toEntity(customerDTO)));

        return customerMapper.toDTO(customer);
    }

    @Override
    public CustomerDTO update(final String id, CustomerDTO customerDTO) {
        Objects.requireNonNull(id, REQUIRE_ID);
        customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        final Customer customer = customerMapper.toEntity(customerDTO);
        customer.setId(id);
        return customerMapper.toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO get(final String id) {
        Objects.requireNonNull(id, REQUIRE_ID);
        final Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        return customerMapper.toDTO(customer);
    }

    @Override
    public void delete(final String id) {
        Objects.requireNonNull(id, REQUIRE_ID);
        customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerDTO> getAll(final Pageable pageable, final String id) {
        Objects.requireNonNull(id, REQUIRE_ID);
        return customerRepository
                .findAllByPhoneNumber(id, pageable)
                .map(customerMapper::toDTO);
    }
}
