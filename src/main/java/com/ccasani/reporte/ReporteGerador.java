package com.ccasani.reporte;

import org.springframework.core.io.InputStreamResource;

public interface ReporteGerador {
    InputStreamResource generateReport();
}
