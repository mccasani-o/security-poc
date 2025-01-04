package com.ccasani.service.impl;

import com.ccasani.model.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuFiltroService {
    public List<Menu> filterActiveMenus(List<Menu> menus) {
        return menus.stream()
                .filter(Menu::isEstado)
                .toList();
    }

    public List<Menu> filterRootMenus(List<Menu> menus) {
        return menus.stream()
                .filter(menu -> menu.getParent() == null) // Solo los menús principales
                .toList();
    }
}
