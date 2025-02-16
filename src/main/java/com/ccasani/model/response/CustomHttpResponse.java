package com.ccasani.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomHttpResponse {
    private LocalDateTime fecha;
    private int statusCode;
    private HttpStatus status;
    private String mensaje;

    private DataResponse data;
}
