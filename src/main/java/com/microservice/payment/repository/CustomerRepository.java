package com.microservice.payment.repository;

import com.microservice.payment.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * customer related query resides here
 *
 * @author Asif Bakht
 * @since 2024
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    /**
     * find custmer by email
     *
     * @param email {@link String} user email
     * @return {@link Optional<Customer>} customer entity
     */
    @Query(value = "SELECT * FROM `customer` c WHERE c.email = :email", nativeQuery = true)
    Optional<Customer> findByEmail(@Param("email") final String email);

    /**
     * find all customer by phone number
     *
     * @param phoneNumber {@link String} phone number
     * @param pageable    {@link Pageable} pagination properties
     * @return {@link Page<Customer>} paginated customer list
     */
    Page<Customer> findAllByPhoneNumber(final String phoneNumber, final Pageable pageable);
}
