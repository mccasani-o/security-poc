package com.ccasani.mapper;

import com.ccasani.model.entity.Categoria;
import com.ccasani.model.entity.MovimientoInventario;
import com.ccasani.model.entity.Producto;
import com.ccasani.model.enumeration.TipoMovimiento;
import com.ccasani.model.request.ProductoRequest;
import com.ccasani.model.response.CategoriaResponse;
import com.ccasani.model.response.MovimientoInventarioResponse;
import com.ccasani.model.response.ProductoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Slf4j
public class ProductoMapper {

    private ProductoMapper() {
    }

    public static ProductoResponse mapToProductoResponse(Producto producto) {
        ProductoResponse productoResponse = ProductoResponse.builder().build();
        BeanUtils.copyProperties(producto, productoResponse);
        productoResponse.setCategoria(producto.getCategoria());
        return productoResponse;
    }

    public static Producto mapToProducto(ProductoRequest request, CategoriaResponse categoriaResponse) {
        Producto productoEntity = Producto.builder().build();
        BeanUtils.copyProperties(request, productoEntity);
        productoEntity.setCategoria(mapToCategoria(categoriaResponse));
        return productoEntity;
    }

    public static CategoriaResponse mapToCategoriaResponse(Categoria categoria) {
        CategoriaResponse categoriaResponse = CategoriaResponse.builder().build();
        BeanUtils.copyProperties(categoria, categoriaResponse);
        return categoriaResponse;
    }

    public static Categoria mapToCategoria(CategoriaResponse categoriaResponse) {
        Categoria categoria = Categoria.builder().build();
        BeanUtils.copyProperties(categoriaResponse, categoria);
        return categoria;
    }

    public static MovimientoInventarioResponse mapToMovimientoInventarioResponse(MovimientoInventario movimientoInventario) {
        MovimientoInventarioResponse response = MovimientoInventarioResponse.builder().build();

        BeanUtils.copyProperties(movimientoInventario, response);
        return response;
    }


    public static MovimientoInventario mapToMovimientoInventario(Producto productoEntity, Integer stockIngreso, TipoMovimiento tipoMovimiento) {
        return MovimientoInventario.builder()
                .idProducto(productoEntity.getIdProducto())
                .tipoMovimiento(tipoMovimiento)
                .cantidad(stockIngreso)
                .fechaMovimiento(LocalDateTime.now())
                .build();
    }
}
