package com.ccasani.resource;

import com.ccasani.model.response.HttpResponse;
import com.ccasani.service.inf.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/menus")
public class MenuResource {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<HttpResponse> menus() {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .fecha(LocalDateTime.now())
                        .data(of("menus", this.menuService.listarMenus()))
                        .mensaje("Lista de menus")
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }


}
