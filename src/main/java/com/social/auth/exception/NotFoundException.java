package com.social.auth.exception;

/**
 * not found exception when user login with email
 * that does not exists in db
 * 
 * @author Asif Bakht
 * @since 2023
 */
public class NotFoundException extends RuntimeException {

   public NotFoundException(String message) {
      super(message);
   }

}
