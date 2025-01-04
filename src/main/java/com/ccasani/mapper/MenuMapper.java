package com.ccasani.mapper;

import com.ccasani.model.entity.Menu;
import com.ccasani.model.response.MenuResponse;
import com.ccasani.service.impl.MenuFiltroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class MenuMapper {
    private final MenuFiltroService menuFilterService;

    public MenuResponse mapToDTO(Menu menu) {
        List<Menu> filteredChildren = menuFilterService.filterActiveMenus(menu.getItems());
        return MenuResponse.builder()
                .id(menu.getId())
                .label(menu.getLabel())
                .url(menu.getUrl())
                .icon(menu.getIcon())
                .estado(menu.isEstado())
                .items(filteredChildren.stream()
                        .map(this::mapToDTO)// Recursión para mapear hijos activos
                        .toList())
                .build();
    }
}
