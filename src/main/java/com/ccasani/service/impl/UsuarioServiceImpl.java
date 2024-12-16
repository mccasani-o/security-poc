package com.ccasani.service.impl;

import com.ccasani.model.entity.Rol;
import com.ccasani.model.entity.Usuario;
import com.ccasani.model.request.LoginRequest;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.model.response.LoginResponse;
import com.ccasani.model.response.UsuarioPrincipal;
import com.ccasani.provider.TokenProvider;
import com.ccasani.repository.RolRepository;
import com.ccasani.repository.UsuarioRepository;
import com.ccasani.service.inf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsService detailsService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    @Override
    public void registarUsuario(RegistrarUsuarioRequest request) {
        Rol rol = Rol.builder().nombre("ROLE_ADMIN").build();
        rolRepository.save(rol);

        this.usuarioRepository.save(this.mapToUsuario(request,rol));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getClave()
        );

        authenticationManager.authenticate(authentication);

        UserDetails user = this.detailsService.loadUserByUsername(request.getEmail());
        String jwt = this.tokenProvider.generateToken(user, generateExtraClaims((UsuarioPrincipal) user));



        return LoginResponse.builder().jwt(jwt).build();
    }

    private Usuario mapToUsuario(RegistrarUsuarioRequest request,Rol rol){
        return Usuario.builder()
                .clave(this.passwordEncoder.encode(request.getClave()))
                .email(request.getEmail())
                .estado(true)
                .roles(Set.of(rol))
                .build();
    }

    private Map<String, Object> generateExtraClaims(UsuarioPrincipal user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name",user.getUsuarioEntity().getEmail());
        extraClaims.put("role",user.getUsuarioEntity().getRoles());
        extraClaims.put("authorities",user.getAuthorities());

        return extraClaims;
    }
}
