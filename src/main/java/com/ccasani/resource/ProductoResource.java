package com.ccasani.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/productos")
public class ProductoResource {

    @PreAuthorize("hasAuthority('READ')")
    @GetMapping
    public String lista(){
        return "Listar";
    }

    @PreAuthorize("hasAuthority('INSERT')")
    @PostMapping
    public String insertar(){
        return "Insertar";
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping
    public String actualizar(){
        return "Actualizar";
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @DeleteMapping
    public String eliminar(){
        return "Eliminar";
    }
}
