package com.microservice.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ResponsePager<T>(
      @Schema(description = "Content") List<T> content,
      @Schema(description = "currentPage") int currentPage,
      @Schema(description = "totalRecords") long totalRecords,
      @Schema(description = "totalRecords") int totalPages) {
}