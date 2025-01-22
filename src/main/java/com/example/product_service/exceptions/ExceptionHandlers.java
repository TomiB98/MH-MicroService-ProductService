package com.example.product_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionHandlers {

    @ExceptionHandler(NoProductsFoundException.class)
    public ResponseEntity<String> productExceptionHandler(NoProductsFoundException noProductsFoundException){
        return new ResponseEntity<>(noProductsFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AllBlanksException.class)
    public ResponseEntity<String> productExceptionHandler(AllBlanksException allBlanksException){
        return new ResponseEntity<>(allBlanksException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProductPriceException.class)
    public ResponseEntity<String> productExceptionHandler(ProductPriceException productPriceException){
        return new ResponseEntity<>(productPriceException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StockException.class)
    public ResponseEntity<String> productExceptionHandler(StockException stockException){
        return new ResponseEntity<>(stockException.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
