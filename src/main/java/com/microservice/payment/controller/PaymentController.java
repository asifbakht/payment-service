package com.microservice.payment.controller;

import com.microservice.payment.dto.Response;
import com.microservice.payment.dto.ResponseError;
import com.microservice.payment.dto.ResponsePager;
import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.exception.GenericException;
import com.microservice.payment.exception.NotFoundException;
import com.microservice.payment.service.impl.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.microservice.payment.utils.Constants.CACHE_PAYMENT;
import static com.microservice.payment.utils.Constants.PAYMENT_SERVICE;
import static com.microservice.payment.utils.Constants.REQUIRE_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Payment controller that provide accessibility
 * to payment apis. This control helps user to
 * cancel, update, add, fetch all their payments
 *
 * @author Asif Bakht
 * @since 2024
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "", produces = APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * This function add user payment received through post api
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @PostMapping()
    @CachePut(value = CACHE_PAYMENT, key = "#id")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<?> addPayment(final @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            log.info("Pay api initiated: {}", paymentDTO.getPaymentMethodId());
            final PaymentDTO responseDTO = paymentService.pay(paymentDTO);
            log.info("Payment add api completed");
            return ResponseEntity.status(HttpStatus.OK).body(new Response<>(responseDTO));
        } catch (final GenericException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage()));
        }
    }

    /**
     * This function update existing pending payment through post api
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @param id         {@link String} payment id
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<?> updatePayment(final @Valid @RequestBody PaymentDTO paymentDTO,
                                           @PathVariable("id") final String id) {
        try {
            log.info("Update payment api initiated: {}", id);
            Objects.requireNonNull(id, "id is required");
            final PaymentDTO responseDTO = paymentService.update(id, paymentDTO);
            log.info("Payment update api completed, {}", id);
            return ResponseEntity.status(HttpStatus.OK).body(new Response<>(responseDTO));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(e.getMessage()));
        } catch (final GenericException | NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage()));
        }
    }

    /**
     * This function cancel user pending payment based on conditions
     *
     * @param id {@link String} payment id
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @GetMapping("/cancel/{id}")
    @Cacheable(value = CACHE_PAYMENT, key = "#id")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<?> cancelPayment(@PathVariable("id") final String id) {
        try {
            log.info("Payment cancel api initiated: {}", id);
            Objects.requireNonNull(id, "id is required");
            final PaymentDTO responseDTO = paymentService.cancel(id);
            log.info("Payment cancelled successfully");
            return ResponseEntity.status(HttpStatus.OK).body(new Response<>(responseDTO));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(e.getMessage()));
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage()));
        }
    }

    /**
     * This function retrieve user payments with pagination
     *
     * @param customerId  {@link String} customer id
     * @param pageRequest {@link Pageable} page size properties
     * @return {@link ResponseEntity<ResponsePager>} response with message or error
     */
    @Operation(summary = "Returns a paginated customer payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/search/{customerId}")
    @Cacheable(value = CACHE_PAYMENT, key = "#customerId")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<?> searchPayments(@PathVariable("customerId") final String customerId,
                                            final Pageable pageRequest) {
        try {
            log.info("Search payments api initiated, customerId: {}", customerId);
            Objects.requireNonNull(customerId, REQUIRE_ID);
            final Page<PaymentDTO> pageCustomers = paymentService.getAllPayments(pageRequest, customerId);
            log.info("Search payments api completed");
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponsePager<>(pageCustomers.getContent(),
                            pageCustomers.getNumber(),
                            pageCustomers.getTotalElements(),
                            pageCustomers.getTotalPages()
                    ));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response<>(e.getMessage()));
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response<>(e.getMessage()));
        }

    }

    /**
     * circuit breaker to avoid error calls for adding payment
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @param e          {@link Throwable} unexpected exception
     * @return {@link ResponseEntity} response entity with 503 error code
     * @throws Throwable {@link Throwable} exception during process
     */
    private ResponseEntity<?> serviceUnavailable(final PaymentDTO paymentDTO,
                                                 final Throwable e) throws Throwable {
        log.error("Could not process payment, customerId: {}, error: {}",
                paymentDTO.getCustomerId(), e.getMessage());
        throw e;
    }

    /**
     * circuit breaker to avoid error calls for updating payment
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @param id         {@link String} payment id
     * @param e          {@link Throwable} unexpected exception
     * @return {@link ResponseEntity} response entity with 503 error code
     * @throws Throwable {@link Throwable} exception during process
     */
    private ResponseEntity<?> serviceUnavailable(final PaymentDTO paymentDTO,
                                                 final String id,
                                                 final Throwable e) throws Throwable {
        log.error("Could not process payment, id: {}, error: {}", id, e.getMessage());
        throw e;
    }

    /**
     * circuit breaker to avoid error calls for cancelling payment
     *
     * @param id {@link String} payment id
     * @param e  {@link Throwable} unexpected exception
     * @return {@link ResponseEntity} response entity with 503 error code
     * @throws Throwable {@link Throwable} exception during process
     */
    private ResponseEntity<?> serviceUnavailable(final String id,
                                                 final Throwable e) throws Throwable {
        log.error("Could not process payment, id: {}, error: {}", id, e.getMessage());
        throw e;
    }

    /**
     * circuit breaker to avoid error calls for cancelling payment
     *
     * @param customerId  {@link String} customer id
     * @param pageRequest {@link Pageable} pagination properties
     * @param e           {@link Throwable} unexpected exception
     * @return {@link ResponseEntity} response entity with 503 error code
     * @throws Throwable {@link Throwable} exception during process
     */
    private ResponseEntity<?> serviceUnavailable(final String customerId,
                                                 final Pageable pageRequest,
                                                 final Throwable e) throws Throwable {
        log.error("Could not process search payment api, customerId: {}, error: {}",
                customerId, e.getMessage());
        throw e;
    }
}
