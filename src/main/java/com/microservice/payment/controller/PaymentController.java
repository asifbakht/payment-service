package com.microservice.payment.controller;

import com.microservice.payment.dto.Response;
import com.microservice.payment.dto.ResponsePager;
import com.microservice.payment.dto.payment.PaymentDTO;
import com.microservice.payment.exception.GenericException;
import com.microservice.payment.exception.NotFoundException;
import com.microservice.payment.service.impl.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.microservice.payment.utils.Constants.CACHE_PAYMENT;
import static com.microservice.payment.utils.Constants.PAYMENT_SERVICE;
import static com.microservice.payment.utils.Constants.REQUIRE_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
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
@RequestMapping(path = "payment", produces = APPLICATION_JSON_VALUE)
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * This function add user payment received through post api
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @Operation(summary = "add payment")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(name = "addPayment",
                                            summary = "Adding payment and returns error if customerId is not present",
                                            value = """
                                                        {
                                                            "statusCode": 400,
                                                            "content": "customerId is required"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "500",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(name = "addPayment",
                                            summary = "When adding payment and occurs unexpected error",
                                            value = """
                                                        {
                                                            "statusCode": 500,
                                                            "content": "Unknown error occurred please try again later"
                                                        }
                                                    """
                                    )
                            }))
    })
    @PostMapping()
    @CachePut(value = CACHE_PAYMENT, key = "#id")
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<Response<?>> addPayment(final @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            log.info("Pay api initiated: {}", paymentDTO.getPaymentMethodId());
            final PaymentDTO responseDTO = paymentService.pay(paymentDTO);
            log.info("Payment add api completed");
            return ResponseEntity
                    .status(CREATED)
                    .body(new Response<>(responseDTO, CREATED.value()));
        } catch (final GenericException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), BAD_REQUEST.value()));
        }
    }

    /**
     * This function update existing pending payment through post api
     *
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @param id         {@link String} payment id
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @Operation(summary = "update payment by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "updatePayment",
                                            summary = "Updating payment and returns error if customerId is not present",
                                            value = """
                                                        {
                                                            "statusCode": 400,
                                                            "content": "customerId is required"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(
                                            name = "updatePayment",
                                            summary = "Updating payment that does not exists",
                                            value = """
                                                        {
                                                            "statusCode": 404,
                                                            "content": "Payment not found"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "500",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(name = "updatePayment",
                                            summary = "When updating payment and occurs unexpected error",
                                            value = """
                                                        {
                                                            "statusCode": 500,
                                                            "content": "Unknown error occurred please try again later"
                                                        }
                                                    """
                                    )
                            }))
    })
    @PutMapping("/{id}")
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<Response<?>> updatePayment(@PathVariable("id") final String id,
                                                     final @Valid @RequestBody PaymentDTO paymentDTO) {
        try {
            log.info("Update payment api initiated: {}", id);
            Objects.requireNonNull(id, "id is required");
            final PaymentDTO responseDTO = paymentService.update(id, paymentDTO);
            log.info("Payment update api completed, {}", id);
            return ResponseEntity
                    .status(OK)
                    .body(new Response<>(responseDTO, OK.value()));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new Response<>(e.getMessage(), NOT_FOUND.value()));
        } catch (final GenericException | NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), BAD_REQUEST.value()));
        }
    }


    /**
     * @return {@link Response} response with status
     */
    @Operation(summary = "get payment information by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "getPayment",
                                            summary = "Get payment and returns error if id is not provided",
                                            value = """
                                                        {
                                                            "statusCode": 400,
                                                            "content": "id is required"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(
                                            name = "getPayment",
                                            summary = "Get payment that does not exists",
                                            value = """
                                                        {
                                                            "statusCode": 404,
                                                            "content": "Payment not found"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "500",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(name = "getPayment",
                                            summary = "When updating payment and occurs unexpected error",
                                            value = """
                                                        {
                                                            "statusCode": 500,
                                                            "content": "Unknown error occurred please try again later"
                                                        }
                                                    """
                                    )
                            }))
    })
    @GetMapping("/{id}")
    @CachePut(value = CACHE_PAYMENT, key = "#id")
    public ResponseEntity<Response<?>> getPayment(@PathVariable("id") final String id) {
        log.info("get customer api: {}", id);
        try {
            log.info("Fetch payment api initiated: {}", id);
            Objects.requireNonNull(id, "id is required");
            final PaymentDTO responseDTO = paymentService.get(id);
            log.info("Fetch api api completed, {}", id);
            return ResponseEntity
                    .status(OK)
                    .body(new Response<>(responseDTO, OK.value()));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new Response<>(e.getMessage(), NOT_FOUND.value()));
        } catch (final GenericException | NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), NOT_FOUND.value()));
        }

    }

    /**
     * This function cancel user pending payment based on conditions
     *
     * @param id {@link String} payment id
     * @return {@link ResponseEntity<Response>} response with message or error
     */
    @Operation(summary = "cancel payment if its in pending state")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "deletePaymentMethod",
                                            summary = "Delete payment and returns success message",
                                            value = """
                                                        {
                                                            "statusCode": 200,
                                                            "content": "Payment method deleted"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "deletePaymentMethod",
                                            summary = "Get payment and returns error if id is not provided",
                                            value = """
                                                        {
                                                            "statusCode": 400,
                                                            "content": "id is required"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(
                                            name = "deletePaymentMethod",
                                            summary = "Get payment that does not exists",
                                            value = """
                                                        {
                                                            "statusCode": 404,
                                                            "content": "Payment not found"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "500",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(name = "deletePaymentMethod",
                                            summary = "When updating payment and occurs unexpected error",
                                            value = """
                                                        {
                                                            "statusCode": 500,
                                                            "content": "Unknown error occurred please try again later"
                                                        }
                                                    """
                                    )
                            }))
    })
    @GetMapping("/cancel/{id}")
    @Cacheable(value = CACHE_PAYMENT, key = "#id")
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    public ResponseEntity<Response<?>> cancelPayment(@PathVariable("id") final String id) {
        try {
            log.info("Payment cancel api initiated: {}", id);
            Objects.requireNonNull(id, "id is required");
            final PaymentDTO responseDTO = paymentService.cancel(id);
            log.info("Payment cancelled successfully");
            return ResponseEntity
                    .status(OK)
                    .body(new Response<>(responseDTO, OK.value()));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new Response<>(e.getMessage(), NOT_FOUND.value()));
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), BAD_REQUEST.value()));
        }
    }

    /**
     * This function retrieve user payments with pagination
     *
     * @param customerId  {@link String} customer id
     * @param pageRequest {@link Pageable} page size properties
     * @return {@link ResponseEntity<ResponsePager>} response with message or error
     */
    @Operation(summary = "Returns a paginated list of customer payments")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "searchPayments",
                                            summary = "Returns a payment lists with pagination",
                                            value = """
                                                        {
                                                            "statusCode": 200,
                                                            "content": ["{payment object}",
                                                                         "{payment object}"],
                                                            "currentPage": 1,
                                                            "totalRecords": 5,
                                                            "totalPage": 10
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            examples = {
                                    @ExampleObject(
                                            name = "searchPayments",
                                            summary = "Get customers method and returns error if customer id is not provided",
                                            value = """
                                                        {
                                                            "statusCode": 400,
                                                            "content": "id is required"
                                                        }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "500",
                    content = @Content(
                            schema = @Schema(implementation = Response.class),
                            examples = {
                                    @ExampleObject(name = "searchPayments",
                                            summary = "When updating payment and occurs unexpected error",
                                            value = """
                                                        {
                                                            "statusCode": 500,
                                                            "content": "Unknown error occurred please try again later"
                                                        }
                                                    """
                                    )
                            }))
    })
    @GetMapping("/search/{customerId}")
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "serviceUnavailable")
    @Cacheable(value = CACHE_PAYMENT, key = "#customerId")
    public ResponseEntity<?> searchPayments(final Pageable pageRequest,
                                            @PathVariable("customerId") final String customerId) {
        try {
            log.info("Search payments api initiated, customerId: {}", customerId);
            Objects.requireNonNull(customerId, REQUIRE_ID);
            final Page<PaymentDTO> pageCustomers = paymentService.getAllPayments(pageRequest, customerId);
            log.info("Search payments api completed");
            return ResponseEntity
                    .status(OK)
                    .body(new ResponsePager<>(pageCustomers.getContent(),
                            pageCustomers.getNumber(),
                            pageCustomers.getTotalElements(),
                            pageCustomers.getTotalPages()
                    ));
        } catch (final NotFoundException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new Response<>(e.getMessage(), NOT_FOUND.value()));
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("Error occurred: {}", e.getMessage());
            return ResponseEntity
                    .status(BAD_REQUEST)
                    .body(new Response<>(e.getMessage(), BAD_REQUEST.value()));
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
    private ResponseEntity<Response<?>> serviceUnavailable(final PaymentDTO paymentDTO,
                                                           final Throwable e) throws Throwable {
        log.error("Could not process payment, customerId: {}, error: {}",
                paymentDTO.getCustomerId(), e.getMessage());
        throw e;
    }

    /**
     * circuit breaker to avoid error calls for updating payment
     *
     * @param id         {@link String} payment id
     * @param paymentDTO {@link PaymentDTO} payment payload
     * @param e          {@link Throwable} unexpected exception
     * @return {@link ResponseEntity} response entity with 503 error code
     * @throws Throwable {@link Throwable} exception during process
     */
    private ResponseEntity<Response<?>> serviceUnavailable(final String id,
                                                           final PaymentDTO paymentDTO,
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
    private ResponseEntity<Response<?>> serviceUnavailable(final String id,
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
    private ResponseEntity<?> serviceUnavailable(final Pageable pageRequest,
                                                 final String customerId,
                                                 final Throwable e) throws Throwable {
        log.error("Could not process search payment api, customerId: {}, error: {}",
                customerId, e.getMessage());
        throw e;
    }
}
