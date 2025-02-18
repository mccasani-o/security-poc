package com.ccasani.reporte.facady.service;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ReporteGerador;
import com.ccasani.reporte.enumeracion.ReporteType;
import com.ccasani.reporte.facady.ReportFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductoReporteService {
    private final ReportFactory reportFactory;


    public ProductoReporteService(ReportFactory reportFactory) {
        this.reportFactory = reportFactory;
    }


    public InputStreamResource generateReport(ReporteType type, List<ProductoResponse> productoResponseList) {
        return reportFactory.getReportGenerator(type).generateReport();
    }
}
