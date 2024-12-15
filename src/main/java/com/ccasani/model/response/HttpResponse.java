package com.ccasani.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse {
    private LocalDateTime fecha;
    private int statusCode;
    private HttpStatus status;
    private String mensaje;
    private Map<?, ?> data;
}
