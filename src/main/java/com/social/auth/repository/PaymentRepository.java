package com.social.auth.repository;

import com.social.auth.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query(value = "SELECT * FROM `payment` p WHERE p.customer_id = :customerId", nativeQuery = true)
    List<Payment> findAllByCustomerId(@Param("customerId") final String customerId);


    @Query(value = "SELECT * FROM `payment` p WHERE p.customer_id = :customerId AND p.status = :status", nativeQuery = true)
    Optional<List<Payment>> findByCustomerAndStatus(@Param("customerId") final String customerId,
                                                    @Param("status") final String status);

    @Query(value = "SELECT * FROM `payment` p WHERE pm.customer_id = :customerId AND pm.account_number = :accountNumber LIMIT 0,1",
            nativeQuery = true)
    Optional<Payment> findByCustomerIdAndAccountNumber(@Param("customerId") final String customerId,
                                                       @Param("accountNumber") final String accountNumber);


    Page<Payment> findAllByCustomerId(final String customerId, final Pageable pageable);

}
