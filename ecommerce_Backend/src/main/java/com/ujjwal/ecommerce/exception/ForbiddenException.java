package com.ujjwal.ecommerce.exception;

// custom exception that represents that the resource may exist, but the current user is not allowed to perform the operation
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}