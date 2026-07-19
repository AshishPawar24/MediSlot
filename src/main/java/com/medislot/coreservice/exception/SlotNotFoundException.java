package com.medislot.coreservice.exception;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException(String message) { super(message); }
}