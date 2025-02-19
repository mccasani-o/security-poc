package com.ccasani.service.specification;

import com.ccasani.model.entity.Producto;
import org.springframework.data.jpa.domain.Specification;

public class ProductoSpecification {
    public static Specification<Producto> hasProducto(String producto) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("nombreProducto"), "%" + producto + "%");
    }
}
