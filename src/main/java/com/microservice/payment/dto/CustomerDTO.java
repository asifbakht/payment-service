package com.microservice.payment.dto;

import jakarta.validation.constraints.Email;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

/**
 * class record that resides customer payload properties
 *
 * @author Asif Bakht
 * @since 2024
 */
public record CustomerDTO(
        String id,
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "email is required")
        @Email(message = "required correct email")
        String email,
        @NotBlank(message = "birth date is required")
        String dateOfBirth,
        @NotBlank(message = "phoneNumber is required")
        String phoneNumber,
        @NotBlank(message = "itin/ssn is required")
        String itinOrSsn
) {
}
