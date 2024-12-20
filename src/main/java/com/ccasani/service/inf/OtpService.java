package com.ccasani.service.inf;

public interface OtpService {
    void sendVerificationCode(long idUsuario);

    boolean isVerificationCodeExpired(String codigo);
}
