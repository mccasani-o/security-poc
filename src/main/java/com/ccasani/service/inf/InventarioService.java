package com.ccasani.service.inf;

import com.ccasani.model.request.ProductoRequest;
import com.ccasani.model.response.*;

import java.util.List;

public interface InventarioService {
    List<CategoriaResponse> listarCategorias();

    CategoriaResponse buscarCategoriaPorId(Integer idCategoria);

    DataResponse productosPaginado(String buscarProducto, Integer numeroPagina, Integer tamanioPagina);

    List<ProductoResponse> listarProductos();

    ProductoResponse buscarProductoPorId(Integer idProducto);

    void agregarProducto(ProductoRequest productoDTO);

    void actualizarProducto(Integer idProducto, ProductoRequest productoDTO);

    void eliminarProducto(Integer idProducto);


    List<MovimientoInventarioResponse> listarMovimientos();


}
