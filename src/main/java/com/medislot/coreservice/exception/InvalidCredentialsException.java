// exception/InvalidCredentialsException.java
package com.medislot.coreservice.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}