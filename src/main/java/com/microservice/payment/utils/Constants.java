package com.microservice.payment.utils;

import com.microservice.payment.exception.NoInstanceException;

/**
 * all static field resides here that will be accessible
 * accross application
 *
 * @author Asif Bakht
 * @since 2024
 */
public final class Constants {

    private Constants() throws NoInstanceException {
        throw new NoInstanceException("Object creation of this class is not allowed");
    }

    public static final String DELIMETER_COMMA = ",";

    /**
     * static variables across application
     */

    public static final String CACHE_PAYMENT = "payment";
    public static final String PAYMENT_SERVICE = "payment-service";

    public static final String REQUIRE_ID = "id cannot be null";

    public static final String ZONE_UTC = "UTC";

    public static final String PAYMENT_NOT_FOUND = "Payment not found";
    public static final double MINIMUM_PAYMENT_AMOUNT = 0.5d;
}
