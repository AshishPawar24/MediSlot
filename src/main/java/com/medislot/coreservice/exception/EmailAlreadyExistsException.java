// exception/EmailAlreadyExistsException.java
package com.medislot.coreservice.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}