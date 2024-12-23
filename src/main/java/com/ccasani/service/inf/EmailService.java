package com.ccasani.service.inf;

import com.ccasani.model.enumeration.TipoVerificacion;

public interface EmailService {
    void sendVerificationEmail(String nombre, String email, String verificacionUrl, TipoVerificacion tipoVerificacion);
}
