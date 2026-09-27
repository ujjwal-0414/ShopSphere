package com.ujjwal.ecommerce.exception;

// custom exception useful when the request conflicts with the current state of the resource
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}