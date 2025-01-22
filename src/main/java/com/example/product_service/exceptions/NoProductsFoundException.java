package com.example.product_service.exceptions;

public class NoProductsFoundException extends Exception {
    public NoProductsFoundException(String message) {
        super(message);
    }
}
