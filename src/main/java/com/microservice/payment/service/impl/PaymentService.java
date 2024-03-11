package com.microservice.payment.service.impl;

import com.microservice.payment.dto.payment.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * payment related business logic resides here
 *
 * @author Asif Bakht
 * @since 2024
 */

public interface PaymentService {

    PaymentDTO pay(final PaymentDTO paymentDTO);

    PaymentDTO cancel(final String id);

    PaymentDTO get(final String id);

    PaymentDTO update(final String id, final PaymentDTO paymentDTO);

    Page<PaymentDTO> getAllPayments(final Pageable pageable, final String customerId);
}
