package com.ccasani.mapper;

import com.ccasani.model.entity.Menu;
import com.ccasani.model.response.MenuResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuMapper {


    public MenuResponse mapToDTO(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .estado(menu.isEstado())
                .items(menu.getItems().stream().filter(Menu::isEstado)
                        .map(this::mapToDTO)// Recursión para mapear hijos activos
                        .toList())
                .build();
    }

    public MenuResponse mapToMenuResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .estado(menu.isEstado())
                .items(menu.getItems().stream()
                        .map(this::mapToMenuResponse)// Recursión para mapear hijos activos
                        .toList())
                .build();
    }
}
