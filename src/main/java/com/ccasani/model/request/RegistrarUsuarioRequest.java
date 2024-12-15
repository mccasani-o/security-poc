package com.ccasani.model.request;

import lombok.Data;

@Data
public class RegistrarUsuarioRequest {
    private String email;
    private String clave;
}
