package com.ccasani.reporte.strategy.service;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.enumeracion.ReporteType;
import com.ccasani.reporte.ProductoExcelReportGenerator;
import com.ccasani.reporte.ProductoPdfReportGenerator;
import com.ccasani.reporte.strategy.context.ReportContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReportService {

    private final ReportContext reportContext;
    private final ProductoExcelReportGenerator excelReportGenerator;
    private final ProductoPdfReportGenerator pdfReportGenerator;

    public ReportService(ReportContext reportContext,
                         ProductoExcelReportGenerator excelReportGenerator,
                         ProductoPdfReportGenerator pdfReportGenerator) {
        this.reportContext = reportContext;
        this.excelReportGenerator = excelReportGenerator;
        this.pdfReportGenerator = pdfReportGenerator;
    }

    public InputStreamResource generateReport(ReporteType type, List<ProductoResponse> productos) {
        switch (type) {
            case EXCEL:
                reportContext.setReporteGerador(excelReportGenerator);
                break;
            case PDF:
                reportContext.setReporteGerador(pdfReportGenerator);
                break;
            default:
                throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        return reportContext.generateReport(productos);
    }
}