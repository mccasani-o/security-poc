package com.ccasani.service.impl;

import com.ccasani.model.entity.Usuario;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.repository.UsuarioRepository;
import com.ccasani.service.inf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void registarUsuario(RegistrarUsuarioRequest request) {
        this.usuarioRepository.save(this.mapToUsuario(request));
    }

    private Usuario mapToUsuario(RegistrarUsuarioRequest request){
        return Usuario.builder()
                .clave(this.passwordEncoder.encode(request.getClave()))
                .email(request.getEmail())
                .estado(true)
                .build();
    }
}
