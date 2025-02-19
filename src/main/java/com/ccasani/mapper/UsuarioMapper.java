package com.ccasani.mapper;

import com.ccasani.model.dto.UsuarioDto;
import com.ccasani.model.entity.Rol;
import com.ccasani.model.entity.Usuario;
import com.ccasani.model.request.RegistrarUsuarioRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Set;

@Slf4j
public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioDto mapToUsuarioDto(Usuario usuario) {
        UsuarioDto usuarioDto= UsuarioDto.builder().build();
        BeanUtils.copyProperties(usuario, usuarioDto);
        log.info("Usuario: {}", usuarioDto);
        return usuarioDto;
    }

    public static Usuario mapToUsuario(RegistrarUsuarioRequest request, Rol rol) {
        return Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .clave(request.getClave())
                .email(request.getEmail())
                .estado(true)
                .roles(Set.of(rol))
                .build();
    }
}
