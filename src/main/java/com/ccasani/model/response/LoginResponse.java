package com.ccasani.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String jwt;
    private boolean isMfa;
    private Long idUsuario;
}
