package com.microservice.payment.mapper;

import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * payment mapper class to avoid boilerplate code
 * this class transform dto to entity and vice versa
 *
 * @author Asif Bakht
 * @since 2024
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper {

    /**
     * converts dto to payment entity class this will ignore
     * status and processing time to populate
     *
     * @param paymentDTO {@link PaymentDTO} payment object
     * @return {@link Payment} payment entity
     */
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "processingTime", ignore = true)
    Payment toEntity(final PaymentDTO paymentDTO);

    /**
     * converts payment entity to dto class
     *
     * @param payment {@link Payment} payment object
     * @return {@link PaymentDTO} payment class
     */
    PaymentDTO toDTO(final Payment payment);

}
