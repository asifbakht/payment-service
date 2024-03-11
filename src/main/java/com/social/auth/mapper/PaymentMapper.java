package com.social.auth.mapper;

import com.social.auth.dto.payment.PaymentDTO;
import com.social.auth.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target="status", ignore = true)
    @Mapping(target="processingTime", ignore = true)
    Payment toEntity(final PaymentDTO paymentDTO);

    PaymentDTO toDTO(final Payment payment);

}
