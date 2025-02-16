package com.ccasani.model.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductoRequest {

    @NotBlank
    private String nombreProducto;
    @NotBlank
    private String descripcion;

    @NotNull(message = "El precio de compra no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de compra debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio de compra debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal precioCompra;

    @NotNull(message = "El precio de venta no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de venta debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "El precio de venta debe tener máximo 10 dígitos enteros y 2 decimales")
    private BigDecimal precioVenta;

    @NotNull(message = "El stock actual no puede ser nulo")
    private Integer stockActual;
    @NotNull(message = "La categoría no puede ser nula")
    private Integer idCategoria;
}
