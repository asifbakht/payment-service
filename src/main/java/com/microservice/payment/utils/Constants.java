package com.microservice.payment.utils;

import com.microservice.payment.exception.NoInstanceException;

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

  public static final String REQUIRE_CUSTOMER_ID = "customerId cannot be null";
  public static final String SUCCESS_DELETE = "Payment method deleted";
  public static final String NOT_FOUND = "payment not found";
  public static final String PAYMENT_ALREADY_EXISTS = "payment already exists";
  public static final String CARD_ALREADY_EXPIRED =  "Card is already expired";
  public static final String CARD_NUMBER_INVALID =  "Card number is invalid";

  public static final String CARD_EXPIRY_INVALID = "Card expiry invalid";

  public static final String NUMERIC_REGEX = "^[0-9]*$";

  public static final String BANK_ACCOUNT_REGEX = "^[0-9]{9,18}$";
  public static final String ROUTING_NUMBER_INVALID = "Routing number is invalid";
  public static final String ACCOUNT_NUMBER_INVALID = "Account number is invalid";

  public static final int ROUTING_LENGTH = 9;

  public static final String ZONE_UTC = "UTC";

  public static final String PAYMENT_NOT_FOUND = "Payment not found";
  public static final double MINIMUM_PAYMENT_AMOUNT = 0.5d;
}
