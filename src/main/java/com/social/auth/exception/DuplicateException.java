package com.social.auth.exception;

/**
 * duplicate exception when user register with same email
 * that already exists in db
 * 
 * @author Asif Bakht
 * @since 2023
 */
public class DuplicateException extends RuntimeException {

   public DuplicateException(String message) {
      super(message);
   }

}
