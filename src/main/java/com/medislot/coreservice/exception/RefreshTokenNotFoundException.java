// exception/RefreshTokenNotFoundException.java
package com.medislot.coreservice.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}