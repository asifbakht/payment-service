package com.social.auth.controller;

import com.social.auth.dto.CustomerDTO;
import com.social.auth.dto.Response;
import com.social.auth.dto.ResponseError;
import com.social.auth.dto.ResponsePager;
import com.social.auth.service.CustomerService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * apis related to customer
 *
 * @author Asif Bakht
 * @since 2024
 */
@RestController
@RequestMapping(path = "api/v1/customer", produces = APPLICATION_JSON_VALUE)
@Log4j2
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * add customer
     *
     * @return {@link  Response<CustomerDTO>}
     */
    @Operation(summary = "customer register api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping()
    public Response<CustomerDTO> add(@Valid @RequestBody final CustomerDTO customerDTO) {
        log.info("add customer api: {}", customerDTO);
        final CustomerDTO responseDTO = customerService.add(customerDTO);
        return new Response<>(responseDTO);
    }


    /**
     * @return {@link Response} response with status
     */
    @Operation(summary = "update customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PutMapping("/{id}")
    public Response<CustomerDTO> update(@PathVariable("id") final String id,
                                        @Valid @RequestBody final CustomerDTO customerDTO) {
        log.info("update customer api: {}", customerDTO);
        final CustomerDTO responseDTO = customerService.update(id, customerDTO);
        return new Response<>(responseDTO);
    }

    /**
     * @return {@link Response} response with status
     */
    @Operation(summary = "get customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/{id}")
    public Response<CustomerDTO> get(@PathVariable("id") final String id) {
        log.info("get customer api: {}", id);
        final CustomerDTO responseDTO = customerService.get(id);
        return new Response<>(responseDTO);
    }

    /**
     * @return {@link Response} response with status
     */
    @Operation(summary = "delete customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @DeleteMapping("/{id}")
    public Response<String> delete(@PathVariable("id") final String id) {
        log.info("get customer api: {}", id);
        customerService.delete(id);
        return new Response<>("Success");
    }


    @Operation(summary = "Returns a paginated customer list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping("/search/{phoneNumber}")
    public ResponsePager<CustomerDTO> searchCustomer(Pageable pageRequest, @PathVariable("phoneNumber") final String phoneNumber) {
        final Page<CustomerDTO> pageCustomers = customerService.getAll(pageRequest, phoneNumber);
        return new ResponsePager<>(pageCustomers.getContent(),
                pageCustomers.getNumber(),
                pageCustomers.getTotalElements(),
                pageCustomers.getTotalPages()
        );
    }
}
