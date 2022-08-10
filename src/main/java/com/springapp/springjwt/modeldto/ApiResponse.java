package com.springapp.springjwt.modeldto;

import lombok.Value;

@Value
public class ApiResponse {
    private Boolean success;
    private String message;
}
