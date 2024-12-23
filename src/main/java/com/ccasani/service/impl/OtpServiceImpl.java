package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.model.dto.UsuarioDto;
import com.ccasani.model.entity.VerificacioneDosFactor;
import com.ccasani.repository.UsuarioRepository;
import com.ccasani.repository.VerificacioneDosFactorRepository;
import com.ccasani.service.inf.EmailService;
import com.ccasani.service.inf.OtpService;
import com.ccasani.util.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.ccasani.mapper.UsuarioMapper.mapToUsuarioDto;
import static com.ccasani.model.enumeration.TipoVerificacion.OTP;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RequiredArgsConstructor
@Service
public class OtpServiceImpl implements OtpService {

    private final VerificacioneDosFactorRepository dosFactoreRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    @Override
    public void sendVerificationCode(long idUsuario) {
        String verificationCode = CodeGenerator.generateVerificationCode(8);
        try {
            this.dosFactoreRepository.deleteById(idUsuario);
            VerificacioneDosFactor verificacioneDosFactor = VerificacioneDosFactor.builder()
                    .codigo(verificationCode)
                    .fechaCaducidad(LocalDateTime.now().plusDays(1))
                    .idUsuario(idUsuario)
                    .build();
            this.dosFactoreRepository.save(verificacioneDosFactor);
            UsuarioDto usuarioDto = this.obtenerUsuarioId(idUsuario);
            this.emailService.sendVerificationEmail(usuarioDto.getNombre(), usuarioDto.getEmail(), verificationCode, OTP);
            log.info("Verification Code: {}", verificationCode);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Ocurrió un error. Inténtalo nuevamente.", BAD_REQUEST.value(), BAD_REQUEST);
        }
    }


    @Override
    public boolean isVerificationCodeExpired(String codigo) {


        VerificacioneDosFactor dosFactorOptional = this.dosFactoreRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ApiException("Este código no se encuentra en las bases. Por favor, vuelva a iniciar sesión.", BAD_REQUEST.value(), BAD_REQUEST));
        // Retorna true si la fecha de caducidad del registro es anterior al momento actual (indicando que el código ha expirado).
        // El método `isBefore` de `LocalDateTime` compara la fecha de caducidad con la hora actual.
        return dosFactorOptional.getFechaCaducidad().isBefore(LocalDateTime.now());


    }

    private UsuarioDto obtenerUsuarioId(Long id) {
        return this.usuarioRepository.findById(id)
                .map(u -> mapToUsuarioDto(u)).orElseThrow(() -> new ApiException("Usuario no encontrado", BAD_REQUEST.value(), BAD_REQUEST));
    }


}
