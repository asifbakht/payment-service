package com.microservice.payment.dto;

import com.microservice.payment.dto.payment.PaymentDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * generic class that is used to return response
 * with content
 *
 * @author Asif Bakht
 * @since 2024
 */
public record Response<T>(
        @Schema(description = "Content", anyOf = { String.class, PaymentDTO.class}) T content) {
}