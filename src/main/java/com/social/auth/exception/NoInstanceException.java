package com.social.auth.exception;

/**
 * Exception that will be used across application whose
 * class object instantiate is restricted
 * 
 * @author Asif Bakht
 * @since 2023
 */
public class NoInstanceException extends RuntimeException {

   public NoInstanceException(final String message) {
      super(message);
   }
}
