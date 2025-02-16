package com.ccasani.model.response;

import com.ccasani.model.entity.Producto;
import com.ccasani.model.enumeration.TipoMovimiento;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MovimientoInventarioResponse {

    private Integer idMovimiento;
    private Producto producto;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private LocalDateTime fechaMovimiento;
}
