package com.microservice.payment.repository;

import com.microservice.payment.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query(value = "SELECT * FROM `payment` p WHERE p.customer_id = :customerId", nativeQuery = true)
    List<Payment> findAllByCustomerId(@Param("customerId") final String customerId);


    @Query(value = "SELECT * FROM `payment` p WHERE p.customer_id = :customerId AND p.status = :status", nativeQuery = true)
    Optional<List<Payment>> findByCustomerAndStatus(@Param("customerId") final String customerId, @Param("status") final String status);

    @Query(value = "SELECT * FROM `payment` p WHERE pm.customer_id = :customerId AND pm.account_number = :accountNumber LIMIT 0,1", nativeQuery = true)
    Optional<Payment> findByCustomerIdAndAccountNumber(@Param("customerId") final String customerId, @Param("accountNumber") final String accountNumber);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `payment` p SET p.status = :status where p.status = :currentStatus", nativeQuery = true)
    void processPendingPayment(@Param("status") final String status, @Param("currentStatus") final String currentStatus);

    Page<Payment> findAllByCustomerId(final String customerId, final Pageable pageable);

}
