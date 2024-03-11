package com.microservice.payment.helper.paymentvalidator;

public interface Command<T> {
    T execute();
}
