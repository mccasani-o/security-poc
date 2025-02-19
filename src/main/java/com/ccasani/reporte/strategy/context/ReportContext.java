package com.ccasani.reporte.strategy.context;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ReporteGerador;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportContext {

    private ReporteGerador reporteGerador;

    public void setReporteGerador(ReporteGerador reporteGerador) {
        this.reporteGerador = reporteGerador;
    }

    public InputStreamResource generateReport(List<ProductoResponse> productos) {
        if (reporteGerador == null) {
            throw new IllegalStateException("No se ha configurado una estrategia de generación de reportes.");
        }
        return reporteGerador.generateReport(productos);
    }
}
