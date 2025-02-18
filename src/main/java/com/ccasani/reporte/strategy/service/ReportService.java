package com.ccasani.reporte.strategy.service;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.enumeracion.ReporteType;
import com.ccasani.reporte.strategy.ProductoExcelReportGenerator;
import com.ccasani.reporte.strategy.ProductoPdfReportGenerator;
import com.ccasani.reporte.strategy.context.ReportContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final ReportContext reportContext;

    public ReportService(ReportContext reportContext) {
        this.reportContext = reportContext;
    }


    public InputStreamResource generateReport(ReporteType type, List<ProductoResponse> productos) {

        switch (type.name().toLowerCase()) {
            case "pdf": reportContext.setReportGenerator(new ProductoPdfReportGenerator(productos));
            break;
            case "excel": reportContext.setReportGenerator(new ProductoExcelReportGenerator(productos));
            break;
            default: throw new IllegalArgumentException("Unsupported report type");
        }
        return reportContext.generateReport();
    }
}
