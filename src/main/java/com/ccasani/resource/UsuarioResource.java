package com.ccasani.resource;

import com.ccasani.model.dto.UsuarioDto;
import com.ccasani.model.request.LoginRequest;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.model.response.HttpResponse;
import com.ccasani.model.response.LoginResponse;
import com.ccasani.service.inf.OtpService;
import com.ccasani.service.inf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioService usuarioService;
    private final OtpService otpService;

    @PostMapping("/registro")
    public ResponseEntity<HttpResponse> registrarUsuario(@RequestBody RegistrarUsuarioRequest request) {
        this.usuarioService.registarUsuario(request);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(CREATED)
                .statusCode(CREATED.value())
                .mensaje("Usuario creado")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = this.usuarioService.login(request);
        return loginResponse.isMfa() ? enviarCodigoDeVerificacion(loginResponse.getIdUsuario()) : enviarJwt(loginResponse);

    }

    @GetMapping("/verificar/{email}/{code}")
    public ResponseEntity<HttpResponse> verifyCode(@PathVariable("email") String email, @PathVariable("code") String code) {
        LoginResponse loginResponse = this.usuarioService.verificarCodigo(email, code);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .data(of("usuario", loginResponse))
                        .mensaje("Inicio de sesión exitoso")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping("/perfil")
    public ResponseEntity<HttpResponse> perfil() {
        UsuarioDto usuarioDto = this.usuarioService.usuarioLogeado();
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .data(of("usuario", usuarioDto))
                        .mensaje("Perfil usuario")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    private ResponseEntity<HttpResponse> enviarJwt(LoginResponse loginResponse) {
        return ResponseEntity.ok().body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(OK)
                .statusCode(OK.value())
                .mensaje("Bienvenido")
                .data(Map.of("token", loginResponse))
                .build());
    }

    private ResponseEntity<HttpResponse> enviarCodigoDeVerificacion(Long idUsuario) {
        this.otpService.sendVerificationCode(idUsuario);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(CREATED)
                .statusCode(CREATED.value())
                .mensaje("Código de verificación enviado")
                .build());
    }


    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/api/usuarios").toUriString());
    }
}
