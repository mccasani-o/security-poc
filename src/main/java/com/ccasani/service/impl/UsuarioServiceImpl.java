package com.ccasani.service.impl;

import com.ccasani.model.Contantes;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        Rol rol = Rol.builder().nombre("ROLE_ADMIN").permiso("READ_PRODUCT").build();
        rolRepository.save(rol);

        this.usuarioRepository.save(this.mapToUsuario(request,rol));
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        if (this.estaBloquedoPorUsuario(request.getEmail())) {
            throw new UsernameNotFoundException("Usuario blokeado");
        }

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    request.getEmail(), request.getClave()
            );

            authenticationManager.authenticate(authentication);
        }catch (Exception exception){
            this.manejoSessionFallido(request.getEmail());
            throw new UsernameNotFoundException("Credenciales incorrectas.");
        }

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

    public void manejoSessionFallido(String email) {
        Usuario usuario = this.usuarioRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            usuario.setIntentosFallido(usuario.getIntentosFallido() + 1);

            if (usuario.getIntentosFallido() >= Contantes.MAX_ATTEMPTS) {
                usuario.setTiempoBloqueo(LocalDateTime.now().plusHours(Contantes.DURACION_TIEMPO_BLOQUEO));
            }
            this.usuarioRepository.save(usuario);
        }
    }


    public boolean estaBloquedoPorUsuario(String email) {
        Optional<Usuario> userOpt = this.usuarioRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();

            if (usuario.getTiempoBloqueo() != null && usuario.getTiempoBloqueo().isAfter(LocalDateTime.now())) {
                return true; // Usuario sigue bloqueado
            }

            // Desbloquear usuario si el tiempo de bloqueo expiró
            if (usuario.getTiempoBloqueo() != null && usuario.getTiempoBloqueo().isBefore(LocalDateTime.now())) {
                usuario.setIntentosFallido(0);
                usuario.setTiempoBloqueo(null);
                this.usuarioRepository.save(usuario);
            }
        }
        return false;
    }

}
