package com.springapp.springjwt.exception.domain;

public class InvalidUsernameFormatException extends Exception{
    public InvalidUsernameFormatException(String message){
        super(message);
    }
}
