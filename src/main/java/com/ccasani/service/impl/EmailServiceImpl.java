package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.model.enumeration.TipoVerificacion;
import com.ccasani.service.inf.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String nombre, String email, String verificacion, TipoVerificacion tipoVerificacion) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            // message.setFrom("<your email>");
            message.setTo(email);
            message.setText(getEmailMessage(nombre, verificacion, tipoVerificacion));
            message.setSubject(String.format("SecureCapita - %s Verification Email", StringUtils.capitalize(tipoVerificacion.getType())));
            mailSender.send(message);
            log.info("Correo electrónico enviado a {}", nombre);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }

    private String getEmailMessage(String nombre, String verificacion, TipoVerificacion tipoVerificacion) {
        switch (tipoVerificacion) {
            case PASSWORD -> { return "Hello " + nombre + "\n\nReset password request. Please click the link below to reset your password. \n\n" + verificacion + "\n\nThe Support Team"; }
            case CUENTA -> { return "Hello " + nombre + "\n\nSe ha creado su nueva cuenta. Haga clic en el enlace que aparece a continuación para verificar su cuenta. \n\n" + verificacion + "\n\nEl equipo de soporte"; }
            case OTP -> { return "Hola " + nombre + "\n\nSu OTP de verificación es \n\n" + verificacion + "\n\nEl equipo de soporte"; }
            default -> throw new ApiException("No se puede enviar el correo electrónico. Tipo de correo electrónico desconocido", BAD_REQUEST.value(), BAD_REQUEST);
        }
    }
}
