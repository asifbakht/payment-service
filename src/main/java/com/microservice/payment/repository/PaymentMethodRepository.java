package com.microservice.payment.repository;

import com.microservice.payment.entity.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {

    @Query(value = "SELECT * FROM `payment_method` pm WHERE pm.customer_id = :customerId", nativeQuery = true)
    List<PaymentMethod> findAllByCustomerId(@Param("customerId") final String customerId);


    @Query(value = "SELECT * FROM `payment_method` pm WHERE pm.customer_id = :customerId AND pm.card_number = :cardNumber LIMIT 0,1", nativeQuery = true)
    Optional<PaymentMethod> findByCustomerIdAndCardNumber(@Param("customerId") final String customerId,
                                                          @Param("cardNumber") final String cardNumber);

    @Query(value = "SELECT * FROM `payment_method` pm WHERE pm.customer_id = :customerId AND pm.account_number = :accountNumber LIMIT 0,1", nativeQuery = true)
    Optional<PaymentMethod> findByCustomerIdAndAccountNumber(@Param("customerId") final String customerId,
                                                          @Param("accountNumber") final String accountNumber);


    Page<PaymentMethod> findAllByCustomerId(final String customerId, final Pageable pageable);

}
