package com.ccasani.reporte;

import com.ccasani.model.response.ProductoResponse;
import org.springframework.core.io.InputStreamResource;

import java.util.List;

public interface ReporteGerador {
    InputStreamResource generateReport(List<ProductoResponse> productoResponses);
}
