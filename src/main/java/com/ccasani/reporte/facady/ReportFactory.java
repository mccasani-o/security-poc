package com.ccasani.reporte.facady;

import com.ccasani.reporte.ReporteGerador;
import com.ccasani.reporte.enumeracion.ReporteType;
import com.ccasani.reporte.ProductoExcelReportGenerator;
import com.ccasani.reporte.ProductoPdfReportGenerator;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class ReportFactory {
    private final Map<ReporteType, ReporteGerador> reportGenerators;


    public ReportFactory(List<ReporteGerador> generators) {
        this.reportGenerators = buildGeneratorsMap(generators);
    }

    private Map<ReporteType, ReporteGerador> buildGeneratorsMap(List<ReporteGerador> generators) {
        Map<ReporteType, ReporteGerador> map = new EnumMap<>(ReporteType.class);

        generators.forEach(generator -> {
            if (generator instanceof ProductoExcelReportGenerator) {
                map.put(ReporteType.EXCEL, generator);
            } else if (generator instanceof ProductoPdfReportGenerator) {
                map.put(ReporteType.PDF, generator);
            }
        });

        return Map.copyOf(map); // Hace el mapa inmutable
    }

    public ReporteGerador getReportGenerator(ReporteType type) {
        ReporteGerador generator = reportGenerators.get(type);
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported report type: " + type);
        }
        return generator;
    }

}