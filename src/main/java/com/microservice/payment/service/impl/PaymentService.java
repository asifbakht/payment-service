package com.microservice.payment.service.impl;

import com.microservice.payment.dto.payment.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    void pay(final PaymentDTO paymentDTO);

    void cancel(final String id);

    void update(final String id, final PaymentDTO paymentDTO);

    Page<PaymentDTO> getAllPayments(final Pageable pageable, final String customerId);
}
