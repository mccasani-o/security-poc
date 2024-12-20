package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.model.entity.Usuario;
import com.ccasani.model.response.UsuarioPrincipal;
import com.ccasani.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new ApiException(
                        String.format("Usuario no encontrado: %s", username),
                        BAD_REQUEST.value(),
                        BAD_REQUEST
                ));

        return new UsuarioPrincipal(usuario);
    }
}
