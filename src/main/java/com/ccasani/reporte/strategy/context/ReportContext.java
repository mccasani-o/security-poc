package com.ccasani.reporte.strategy.context;
import com.ccasani.reporte.ReporteGerador;

import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

@Component
public class ReportContext {

    private ReporteGerador reportGenerator;


    public void setReportGenerator(ReporteGerador reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    public InputStreamResource generateReport() {
        if (reportGenerator == null) {
            throw new IllegalStateException("No report generator selected");
        }
        return reportGenerator.generateReport();
    }
}
