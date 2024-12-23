package com.ccasani.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoVerificacion {
    CUENTA("CUENTA"),
    OTP("OTP"),
    PASSWORD("PASSWORD");

    private final String type;


}
