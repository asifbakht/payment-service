package com.microservice.payment.exception;

import com.microservice.payment.dto.ResponseError;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.microservice.payment.utils.Constants.DELIMETER_COMMA;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * exception thrown from business services or controller
 * will handler here
 *
 * @author Asif Bakht
 * @since 2024
 */
@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * exception handler when specific resource not exists
     *
     * @param e {@link NotFoundException} exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFoundException(final NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND).body(new ResponseError(NOT_FOUND.value(), e.getMessage()));
    }

    /**
     * exception handler when payload field validation invokes
     *
     * @param e {@link MethodArgumentNotValidException} exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleRequestNotValidException(final MethodArgumentNotValidException e) {

        List<String> errors = new ArrayList<>();
        e.getBindingResult()
                .getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        e.getBindingResult()
                .getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        final String message = "Payload misses information: %s".formatted(String.join(DELIMETER_COMMA, errors));
        return ResponseEntity.status(BAD_REQUEST).body(new ResponseError(BAD_REQUEST.value(), message));
    }

    /**
     * exception handler when user with same resource already exists
     *
     * @param {@link DuplicateException} custom duplicate exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ResponseError> handleDuplicateException(final DuplicateException e) {
        return ResponseEntity.status(CONFLICT).body(new ResponseError(CONFLICT.value(), e.getMessage()));
    }

    /**
     * exception handler that catches generic exception within entire application
     * will be handled here
     *
     * @param {@link GenericException} custom duplicate exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ResponseError> handleGenericException(final GenericException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ResponseError(BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * exception handler when unexpected data is provided will be handled here
     *
     * @param {@link GenericException} custom duplicate exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseError> handleIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.status(BAD_REQUEST).body(new ResponseError(BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * unknown/unexpected handler
     *
     * @param {@link Exception} generic exception
     * @return {@link ResponseEntity} response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseError> handleUnknownException(final Exception e) {
        log.error("Error occurred: {}", e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ResponseError(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}