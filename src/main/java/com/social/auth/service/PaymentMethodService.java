package com.social.auth.service;

import com.social.auth.dto.paymentmethod.PaymentMethodDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentMethodService {

    public PaymentMethodDTO add(final PaymentMethodDTO customerDTO);
    public PaymentMethodDTO update(final String id, final PaymentMethodDTO customerDTO);
    public PaymentMethodDTO get(final String id);
    public void delete(final String id);

    public Page<PaymentMethodDTO> getAll(final Pageable pageable, final String id);
}
