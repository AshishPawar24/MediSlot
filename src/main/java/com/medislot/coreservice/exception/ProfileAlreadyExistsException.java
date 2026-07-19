// exception/ProfileAlreadyExistsException.java
package com.medislot.coreservice.exception;

public class ProfileAlreadyExistsException extends RuntimeException {
    public ProfileAlreadyExistsException(String message) {
        super(message);
    }
}