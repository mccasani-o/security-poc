package com.ccasani.repository;

import com.ccasani.model.entity.VerificacioneDosFactor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificacioneDosFactorRepository extends JpaRepository<VerificacioneDosFactor, Long> {


    Optional<VerificacioneDosFactor> findByCodigo(String codigo);

    void deleteByCodigo(String codigo);
}
