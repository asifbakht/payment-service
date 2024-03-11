package com.microservice.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseError(
      @Schema(description = "Error code") int errorCode,
      @Schema(description = "Error description") String description) {
}