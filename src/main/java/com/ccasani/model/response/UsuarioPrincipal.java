package com.ccasani.model.response;

import com.ccasani.model.entity.Usuario;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UsuarioPrincipal implements UserDetails {
    private Usuario usuarioEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.usuarioEntity.getRoles().stream() // Stream de roles
                .flatMap(rol -> Arrays.stream(rol.getPermiso().split(","))) // Dividir permisos y aplanarlos en un Stream
                .map(String::trim) // Eliminar espacios en blanco alrededor de cada permiso
                .map(SimpleGrantedAuthority::new) // Crear SimpleGrantedAuthority para cada permiso
                .toList(); // Recopilar en una lista

    }

    @Override
    public String getPassword() {
        return this.usuarioEntity.getClave();
    }

    @Override
    public String getUsername() {
        return this.usuarioEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.usuarioEntity.isEstado();
    }


}
