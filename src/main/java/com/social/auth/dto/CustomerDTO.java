package com.social.auth.dto;

import jakarta.validation.constraints.Email;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

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
