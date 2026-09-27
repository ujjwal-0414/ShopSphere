package com.ujjwal.ecommerce.exception;

// custom exception that represents a request that is syntactically valid but violates a business rule
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

}