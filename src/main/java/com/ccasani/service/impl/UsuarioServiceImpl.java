package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.model.Contantes;
import com.ccasani.model.dto.UsuarioDto;
import com.ccasani.model.entity.Rol;
import com.ccasani.model.entity.Usuario;
import com.ccasani.model.entity.VerificacioneDosFactor;
import com.ccasani.model.request.LoginRequest;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import com.ccasani.model.response.LoginResponse;
import com.ccasani.model.response.UsuarioPrincipal;
import com.ccasani.provider.TokenProvider;
import com.ccasani.repository.RolRepository;
import com.ccasani.repository.UsuarioRepository;
import com.ccasani.repository.VerificacioneDosFactorRepository;
import com.ccasani.service.inf.OtpService;
import com.ccasani.service.inf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ccasani.mapper.UsuarioMapper.mapToUsuario;
import static com.ccasani.mapper.UsuarioMapper.mapToUsuarioDto;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UserDetailsService detailsService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final OtpService otpService;
    private final VerificacioneDosFactorRepository dosFactoreRepository;


    @Override
    public void registarUsuario(RegistrarUsuarioRequest request) {
        Rol rol = Rol.builder().nombre("ROLE_USER").permiso("READ,UPDATE").build();
        rolRepository.save(rol);
        request.setClave( this.passwordEncoder.encode(request.getClave()));
        this.usuarioRepository.save(mapToUsuario(request, rol));
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        if (this.estaBloquedoPorUsuario(request.getEmail())) {
            throw new ApiException("Usuario blokeado", LOCKED.value(), LOCKED);
        }

        this.authenticate(request.getEmail(), request.getClave());

        UserDetails userDetails = this.detailsService.loadUserByUsername(request.getEmail());
        String jwt = this.tokenProvider.generateToken(userDetails, generateExtraClaims((UsuarioPrincipal) userDetails));

        return LoginResponse.builder().jwt(jwt)
                .isMfa(((UsuarioPrincipal) userDetails).getUsuarioEntity().isMfa())
                .idUsuario(((UsuarioPrincipal) userDetails).getUsuarioEntity().getId())
                .build();
    }

    @Transactional
    @Override
    public LoginResponse verificarCodigo(String email, String codigo) {
        if (this.otpService.isVerificationCodeExpired(codigo)) {
            throw new ApiException("Este código ha expirado. Por favor, vuelva a iniciar sesión.", BAD_REQUEST.value(), BAD_REQUEST);
        }

        VerificacioneDosFactor dosFactorOptional = this.dosFactoreRepository.findByCodigo(codigo)
                .orElseThrow(() -> new ApiException("El código no es válido. Inténtalo de nuevo.", BAD_REQUEST.value(), BAD_REQUEST));

        Usuario usuario = this.usuarioRepository.findById(dosFactorOptional.getIdUsuario())
                .orElseThrow(() -> new ApiException(String.format("No se encontró el usuario con el id: %s", dosFactorOptional.getIdUsuario()), BAD_REQUEST.value(), BAD_REQUEST));

        if (email.equals(usuario.getEmail())) {
            this.dosFactoreRepository.deleteByCodigo(dosFactorOptional.getCodigo());

            UserDetails userDetails = this.detailsService.loadUserByUsername(usuario.getEmail());
            String jwt = this.tokenProvider.generateToken(userDetails, generateExtraClaims((UsuarioPrincipal) userDetails));

            return LoginResponse.builder().jwt(jwt)
                    .isMfa(((UsuarioPrincipal) userDetails).getUsuarioEntity().isMfa())
                    .idUsuario(((UsuarioPrincipal) userDetails).getUsuarioEntity().getId())
                    .build();
        } else {
            throw new ApiException("El email es incorrecto.", BAD_REQUEST.value(), BAD_REQUEST);
        }


    }

    @Override
    public UsuarioDto usuarioLogeado() {
        return this.obtenerUsuario();
    }


    private UsuarioDto obtenerUsuario() {
        String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuario = this.usuarioRepository.findByEmail(principal).orElseThrow();
        return mapToUsuarioDto(usuario);
    }


    private Authentication authenticate(String email, String password) {
        try {
            return authenticationManager.authenticate(unauthenticated(email, password));

        } catch (Exception exception) {
            this.manejoSessionFallido(email);
            throw new ApiException("Credenciales incorrectas.", FORBIDDEN.value(), FORBIDDEN);
        }
    }


    private Map<String, Object> generateExtraClaims(UsuarioPrincipal user) {
        String rolesAsString = user.getUsuarioEntity().getRoles()
                .stream() // Convertir la lista de roles a un Stream
                .map(Rol::getNombre) // Obtener el nombre del rol (asumiendo que Rol tiene un método getName)
                .collect(Collectors.joining(",")); // Unir los nombres en un solo String separado por comas
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getUsuarioEntity().getEmail());
        extraClaims.put("role", rolesAsString);
        extraClaims.put("authorities", user.getAuthorities());

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
