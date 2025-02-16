package com.ccasani.model.response;

import com.ccasani.model.entity.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResponse {

    private Integer idProducto;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockActual;
    private Categoria categoria;

}
