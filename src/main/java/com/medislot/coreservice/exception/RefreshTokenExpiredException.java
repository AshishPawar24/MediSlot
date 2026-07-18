// exception/RefreshTokenExpiredException.java
package com.medislot.coreservice.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}