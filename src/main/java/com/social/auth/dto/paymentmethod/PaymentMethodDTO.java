package com.social.auth.dto.paymentmethod;


import com.social.auth.helper.enumvalidator.EnumValidator;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 *
 */
@Value
public class PaymentMethodDTO {

    String id;
    @NotBlank(message = "customerId is required")
    String customerId;
    @NotBlank(message = "paymentName is required")
    String paymentName;
    @EnumValidator(enumClazz = PayType.class, message = "Invalid payment type")
    String paymentType;
    String cardHolderName;
    String cardNumber;
    Integer expirationMonth;
    Integer expirationYear;
    String cvv;
    String cardType;
    String accountNumber;
    String routingNumber;
    String accountHolderName;
    String bankName;
}