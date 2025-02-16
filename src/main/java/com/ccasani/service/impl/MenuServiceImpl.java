package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.mapper.MenuMapper;
import com.ccasani.model.entity.Menu;
import com.ccasani.model.request.MenuRequest;
import com.ccasani.model.response.MenuResponse;
import com.ccasani.repository.MenuRepository;
import com.ccasani.service.inf.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuFiltroService menuFiltroService;
    private final MenuMapper menuMapper;

    @Override
    public List<MenuResponse> obtenerMenuPorEstado() {
        List<Menu> allMenus = menuRepository.findAll().stream().filter(Menu::isEstado).toList();
        List<Menu> activeRootMenus = menuFiltroService.filterRootMenus(allMenus);
        return activeRootMenus.stream()
                .map(menuMapper::mapToDTO)
                .toList();
    }


    @Override
    public List<MenuResponse> obtenerMenus() {
        List<Menu> activeRootMenus = menuFiltroService.filterRootMenus(this.menuRepository.findAll());
        return activeRootMenus.stream()
                .map(menuMapper::mapToMenuResponse)
                .toList();
    }


    @Override
    public void actualizarEstado(MenuRequest request) {
        request.getEstadoRequests().forEach(m->{
            Menu menu = this.menuRepository.findById(m.getId()).orElseThrow(() -> new ApiException("El id no esta en las bases del sistema", BAD_REQUEST.value(), BAD_REQUEST));
            menu.setEstado(m.isEstado());
            this.menuRepository.save(menu);
        });
    }


}
