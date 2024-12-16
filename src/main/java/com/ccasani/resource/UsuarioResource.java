package com.ccasani.resource;

import com.ccasani.model.request.LoginRequest;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.model.response.HttpResponse;
import com.ccasani.model.response.LoginResponse;
import com.ccasani.service.inf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioService usuarioService;
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
        LoginResponse loginResponse=this.usuarioService.login(request);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(OK)
                .statusCode(OK.value())
                .mensaje("Login exitoso")
                .data(Map.of("token",loginResponse))
                .build());
    }


    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/api/usuarios").toUriString());
    }
}
