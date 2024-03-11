package com.microservice.payment.controller;

import com.microservice.payment.dto.Response;
import com.microservice.payment.dto.ResponseError;
import com.microservice.payment.dto.ResponsePager;
import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.service.impl.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.microservice.payment.utils.Constants.REQUIRE_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "", produces = APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping()
    public Response<String> addPayment(final @Valid @RequestBody PaymentDTO paymentDTO) {
//        LOGGER.info("Payment accepted: " + payment.toString());
        log.info("Pay api initiated: {}", paymentDTO.getPaymentMethodId());
        paymentService.pay(paymentDTO);
        return new Response<>("Payment added successfully");
    }


    @PutMapping("/{id}")
    public Response<String> updatePayment(final @Valid @RequestBody PaymentDTO paymentDTO,
                                          @PathVariable("id") final String id) {
        log.info("Update payment api initiated: {}", id);
        Objects.requireNonNull(id, "id is required");
        paymentService.update(id, paymentDTO);
        return new Response<>("Payment updated successfully");
    }

    @GetMapping("/cancel/{id}")
    public Response<String> cancelPayment(@PathVariable("id") final String id) {
        Objects.requireNonNull(id, "id is required");
        log.info("Payment cancel api initiated: {}", id);
        paymentService.cancel(id);
        log.info("Payment cancelled successfully");
        return new Response<>("Payment cancelled successfully");
    }

    @Operation(summary = "Returns a paginated customer payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/search/{customerId}")
    public ResponsePager<PaymentDTO> searchPayments(final Pageable pageRequest,
                                                    @PathVariable("customerId") final String customerId) {
        log.info("Search payments api initiated, customerId: {}", customerId);
        Objects.requireNonNull(customerId, REQUIRE_ID);
        final Page<PaymentDTO> pageCustomers = paymentService.getAllPayments(pageRequest, customerId);
        log.info("Search payments api completed");
        return new ResponsePager<>(pageCustomers.getContent(),
                pageCustomers.getNumber(),
                pageCustomers.getTotalElements(),
                pageCustomers.getTotalPages()
        );
    }

    @GetMapping("/health")
    public String getHealth() {
//        LOGGER.info("Payment accepted: " + payment.toString());
        return "Online hai";
    }
}
