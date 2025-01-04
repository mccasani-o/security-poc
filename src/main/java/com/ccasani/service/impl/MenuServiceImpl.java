package com.ccasani.service.impl;

import com.ccasani.mapper.MenuMapper;
import com.ccasani.model.entity.Menu;
import com.ccasani.model.response.MenuResponse;
import com.ccasani.repository.MenuRepository;
import com.ccasani.service.inf.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuFiltroService menuFiltroService;
    private final MenuMapper menuMapper;

    @Override
    public List<MenuResponse> listarMenus() {
        List<Menu> allMenus = menuRepository.findAll();
        List<Menu> activeRootMenus = menuFiltroService.filterRootMenus(menuFiltroService.filterActiveMenus(allMenus));
        return activeRootMenus.stream()
                .map(menuMapper::mapToDTO)
                .toList();
    }


}
