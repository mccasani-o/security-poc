package com.ccasani.model.response;

import com.ccasani.model.entity.Usuario;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UsuarioPrincipal implements UserDetails {
    private Usuario usuarioEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        usuarioEntity.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority(role.getNombre())));
        return authorityList;
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
        return true;
    }

    public static UsuarioPrincipal mapToUsuarioPrincipal(Usuario usuario){

        return new UsuarioPrincipal(usuario);
    }
}
