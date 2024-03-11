package com.microservice.payment.mapper;

import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target="status", ignore = true)
    @Mapping(target="processingTime", ignore = true)
    Payment toEntity(final PaymentDTO paymentDTO);

    PaymentDTO toDTO(final Payment payment);

}
