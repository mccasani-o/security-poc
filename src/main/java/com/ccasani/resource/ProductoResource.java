package com.ccasani.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/productos")
public class ProductoResource {

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public String admin(){
        return "ADMIN";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String user(){
        return "USER";
    }
}
