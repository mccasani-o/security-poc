package com.ccasani.resource;

import com.ccasani.model.request.MenuRequest;
import com.ccasani.model.response.HttpResponse;
import com.ccasani.service.inf.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/menus")
public class MenuResource {

    private final MenuService menuService;

    @GetMapping("/activo")
    public ResponseEntity<HttpResponse> obtenerMenuPorEstado() {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .data(of("menus", this.menuService.obtenerMenuPorEstado()))
                        .mensaje("Lista de menus")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @GetMapping
    public ResponseEntity<HttpResponse> obtenerMenus() {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .data(of("menus", this.menuService.obtenerMenus()))
                        .mensaje("Lista de menus")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }

    @PutMapping
    public ResponseEntity<HttpResponse> actualizar(@RequestBody MenuRequest request) {
        this.menuService.actualizarEstado(request);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .mensaje("Actualización satisfactorio.")
                        .status(NO_CONTENT)
                        .statusCode(NO_CONTENT.value())
                        .build());
    }

}
