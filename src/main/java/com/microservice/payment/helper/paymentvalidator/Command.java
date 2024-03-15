package com.microservice.payment.helper.paymentvalidator;

/**
 * Command pattern for payment validator
 * this pattern is designed to avoid code
 * duplication
 *
 * @param <T> generic param
 */
public interface Command<T> {
    T execute();
}
