package com.microservice.payment.service.impl.impl;

import com.microservice.payment.dto.CustomerDTO;
import com.microservice.payment.entity.Customer;
import com.microservice.payment.exception.NotFoundException;
import com.microservice.payment.mapper.CustomerMapper;
import com.microservice.payment.repository.CustomerRepository;
import com.microservice.payment.service.impl.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Customer crud operation functionality resides here
 *
 * @author Asif Bakht
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private static final String NOT_FOUND = "customer not found";
    private static final String REQUIRE_ID = "id cannot be null";

    /**
     * add customer to database, it will check if same
     * email already exists
     *
     * @param customerDTO {@link CustomerDTO} customer payload
     * @return {@link CustomerDTO} customer payload with id
     */
    @Override
    public CustomerDTO add(CustomerDTO customerDTO) {
        final Customer customer = customerRepository
                .findByEmail(customerDTO.email())
                .orElseGet(() -> customerRepository
                        .save(customerMapper.toEntity(customerDTO)));

        return customerMapper.toDTO(customer);
    }

    /**
     * update customer to database, it will check if customer
     * exists or not
     *
     * @param id          {@link String} customer id
     * @param customerDTO {@link CustomerDTO} updated customer payload
     * @return {@link CustomerDTO} customer updated payload
     */
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

    /**
     * get customer information by customer id
     *
     * @param id {@link String} customer id
     * @return {@link CustomerDTO} customer dto object
     */
    @Override
    public CustomerDTO get(final String id) {
        Objects.requireNonNull(id, REQUIRE_ID);
        final Customer customer = customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        return customerMapper.toDTO(customer);
    }

    /**
     * delete customer information from database
     *
     * @param id {@link String} customer id
     */
    @Override
    public void delete(final String id) {
        Objects.requireNonNull(id, REQUIRE_ID);
        customerRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND));
        customerRepository.deleteById(id);
    }

    /**
     * retrieve all customer information by phone numnber
     *
     * @param pageable    {@link Pageable} pagination properties
     * @param phoneNumber {@link String} phone number
     * @return
     */
    @Override
    public Page<CustomerDTO> getAll(final Pageable pageable, final String phoneNumber) {
        Objects.requireNonNull(phoneNumber, REQUIRE_ID);
        return customerRepository
                .findAllByPhoneNumber(phoneNumber, pageable)
                .map(customerMapper::toDTO);
    }
}
