package com.social.auth.dto.payment;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Value
public class PaymentDTO {

    private String id;
    @NotBlank(message = "customerId is required")
    private String customerId;
    @NotNull(message = "amount is required")
    private Double amount;
    @NotBlank(message = "paymentMethodId is required")
    private String paymentMethodId;

    private String status;
    private String processingTime;
}
