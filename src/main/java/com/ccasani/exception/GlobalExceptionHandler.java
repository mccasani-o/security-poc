package com.ccasani.exception;

import com.ccasani.model.response.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> handlerAccessDeniedException(AccessDeniedException exception) {
        HttpResponse apiError = HttpResponse.builder()
                .status(FORBIDDEN)
                .statusCode(FORBIDDEN.value())
                .mensaje("Comuniquese con el administrador si crees que esto es un error.")
                .fecha(LocalDateTime.now())
                .build();


        return ResponseEntity.status(FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<HttpResponse> apiException(ApiException exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(HttpResponse.builder()
                .fecha(LocalDateTime.now())
                .mensaje(exception.getMessage())
                .status(exception.getStatus())
                .statusCode(exception.getStatusCode())
                .build(), exception.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> exception(Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .fecha(now())
                        .mensaje(exception.getMessage() != null ?
                                (exception.getMessage().contains("expected 1, actual 0") ? "Record not found" : exception.getMessage())
                                : "Some error occurred")
                        .status(INTERNAL_SERVER_ERROR)
                        .statusCode(INTERNAL_SERVER_ERROR.value())
                        .build(), INTERNAL_SERVER_ERROR);
    }
}
