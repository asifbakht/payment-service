package com.microservice.payment.dto;

import com.microservice.payment.dto.payment.PaymentDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

/**
 * generic class that is used to return response
 * with content
 *
 * @author Asif Bakht
 * @since 2024
 */
@Value
public class Response<T> {
    @Schema(description = "Content", anyOf = {PaymentDTO.class, String.class})
    private T content;

    @Schema(description = "Status code")
    private int statusCode;
}