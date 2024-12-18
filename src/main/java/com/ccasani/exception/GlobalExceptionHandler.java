package com.ccasani.exception;

import com.ccasani.model.response.HttpResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException(AccessDeniedException exception) {

        HttpResponse apiError = HttpResponse.builder()
                .status(FORBIDDEN)
                .statusCode(FORBIDDEN.value())
                .mensaje("ministrador si crees que esto es un error.")
                .fecha(LocalDateTime.now())
                .build();


        return ResponseEntity.status(FORBIDDEN).body(apiError);
    }
}
