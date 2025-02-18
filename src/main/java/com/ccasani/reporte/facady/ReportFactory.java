package com.ccasani.reporte.facady;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ReporteGerador;
import com.ccasani.reporte.enumeracion.ReporteType;
import com.ccasani.reporte.strategy.ProductoExcelReportGenerator;
import com.ccasani.reporte.strategy.ProductoPdfReportGenerator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

import java.util.Map;



@Component
public class ReportFactory {
    private final Map<ReporteType, ReporteGerador> reportGenerators;


    public ReportFactory() {
        this.reportGenerators = new HashMap<>();
        this.reportGenerators.put(ReporteType.PDF, new ProductoPdfReportGenerator(null));
        this.reportGenerators.put(ReporteType.EXCEL, new ProductoExcelReportGenerator(null));
    }


    public ReporteGerador getReportGenerator(ReporteType type) {
        ReporteGerador generator = reportGenerators.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        return generator;
    }
}
