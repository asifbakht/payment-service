package com.social.auth.controller;

import com.social.auth.dto.ResponseError;
import com.social.auth.dto.ResponsePager;
import com.social.auth.dto.paymentmethod.PaymentMethodDTO;
import com.social.auth.service.PaymentMethodService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.social.auth.utils.Constants.REQUIRE_ID;
import static com.social.auth.utils.Constants.SUCCESS_DELETE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * apis related to payment
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/v1/payment-method", produces = APPLICATION_JSON_VALUE)
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;


    /**
     * add payment method to database
     *
     * @return {@link PaymentMethodDTO}
     */
    @Operation(summary = "add payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping
    public PaymentMethodDTO addPayment(@Valid @RequestBody final PaymentMethodDTO paymentmethodDTO) {
        log.info("Add payment api initiated");
        final PaymentMethodDTO paymentDTO = paymentMethodService.add(paymentmethodDTO);
        log.info("Add payment api completed");
        return paymentDTO;
    }

    /**
     * @return {@link PaymentMethodDTO} response with status
     */
    @Operation(summary = "update payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PutMapping("/{id}")
    public PaymentMethodDTO update(@PathVariable("id") final String id,
                                   @Valid @RequestBody final PaymentMethodDTO paymentMethodDTO) {
        log.info("Update payment api initiated: {}", id);
        Objects.requireNonNull(id, REQUIRE_ID);
        final PaymentMethodDTO responseDTO = paymentMethodService.update(id, paymentMethodDTO);
        log.info("Update payment api completed: {}", id);
        return responseDTO;
    }

    /**
     * @return {@link PaymentMethodDTO} response with status
     */
    @Operation(summary = "get payment method by providing id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/{id}")
    public PaymentMethodDTO get(@PathVariable("id") final String id) {
        log.info("Get payment method initiated: {}", id);
        Objects.requireNonNull(id, REQUIRE_ID);
        final PaymentMethodDTO responseDTO = paymentMethodService.get(id);
        log.info("Get payment method completed: {}", id);
        return responseDTO;
    }

    /**
     * @return {@link PaymentMethodDTO} response with status
     */
    @Operation(summary = "delete payment method information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") final String id) {
        log.info("Delete payment method api initiated: {}", id);
        Objects.requireNonNull(id, REQUIRE_ID);
        paymentMethodService.delete(id);
        log.info("Delete payment method api completed: {}", id);
        return SUCCESS_DELETE;
    }

    @Operation(summary = "Returns a paginated list of payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/search/{customerId}")
    public ResponsePager<PaymentMethodDTO> searchPayments(final Pageable pageRequest,
                                                          @PathVariable("customerId") final String customerId) {
        log.info("Search customer payment method api initiated: {}", customerId);
        Objects.requireNonNull(customerId, REQUIRE_ID);
        final Page<PaymentMethodDTO> pageCustomers = paymentMethodService.getAll(pageRequest, customerId);
        return new ResponsePager<>(pageCustomers.getContent(),
                pageCustomers.getNumber(),
                pageCustomers.getTotalElements(),
                pageCustomers.getTotalPages()
        );
    }
}
