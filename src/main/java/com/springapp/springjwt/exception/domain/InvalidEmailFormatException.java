package com.springapp.springjwt.exception.domain;

public class InvalidEmailFormatException extends Exception{
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
