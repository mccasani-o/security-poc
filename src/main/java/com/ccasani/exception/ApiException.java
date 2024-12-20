package com.ccasani.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiException extends RuntimeException{


    private final int statusCode;
    private final HttpStatus status;



    public ApiException(String message, int statusCode, HttpStatus status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }
}
