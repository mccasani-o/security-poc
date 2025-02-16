package com.ccasani.service.inf;

import com.ccasani.model.request.MenuRequest;
import com.ccasani.model.response.MenuResponse;

import java.util.List;


public interface MenuService {

     List<MenuResponse> obtenerMenuPorEstado();

     List<MenuResponse> obtenerMenus();
     void actualizarEstado(MenuRequest request);
}
