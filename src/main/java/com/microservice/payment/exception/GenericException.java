package com.microservice.payment.exception;

/**
 * duplicate exception when user register with same email
 * that already exists in db
 *
 * @author Asif Bakht
 * @since 2023
 */
public class GenericException extends RuntimeException {

    public GenericException(String message) {
        super(message);
    }

}