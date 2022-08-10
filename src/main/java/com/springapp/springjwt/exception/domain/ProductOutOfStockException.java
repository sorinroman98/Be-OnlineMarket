package com.springapp.springjwt.exception.domain;

public class ProductOutOfStockException extends Exception{
    public ProductOutOfStockException(String message) {
        super(message);
    }
}
