package com.microservice.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record Response<T>(
      @Schema(description = "Content") T content) {
}