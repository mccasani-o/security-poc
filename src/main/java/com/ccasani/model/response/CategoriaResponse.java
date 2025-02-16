package com.ccasani.model.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CategoriaResponse {

    private Integer idCategoria;
    private String nombreCategoria;
}
