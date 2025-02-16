package com.ccasani.repository;

import com.ccasani.model.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer> {
    @Transactional
    void deleteByIdProducto(Integer idProducto);
}
