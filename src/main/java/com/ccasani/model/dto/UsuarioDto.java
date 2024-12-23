package com.ccasani.model.dto;

import com.ccasani.model.entity.Rol;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private boolean estado;
    private boolean noEstaBloqueado;
    private boolean isMfa;
    private int intentosFallido;
    private LocalDateTime tiempoBloqueo;
    private Set<Rol> roles;
}
