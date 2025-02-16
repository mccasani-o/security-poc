package com.ccasani.model.entity;

import com.ccasani.model.enumeration.TipoMovimiento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "movimientos_inventario")
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoInventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;
    /*
    @ManyToOne
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

     */
    private Integer idProducto;

    @Column(name = "tipo_movimiento", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;
}
