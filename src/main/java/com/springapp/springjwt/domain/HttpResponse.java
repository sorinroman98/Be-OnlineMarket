package com.springapp.springjwt.domain;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class HttpResponse {
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

}
