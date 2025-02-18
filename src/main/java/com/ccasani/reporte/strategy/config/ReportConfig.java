package com.ccasani.reporte.strategy.config;

import com.ccasani.reporte.strategy.context.ReportContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfig {
    @Bean
    public ReportContext reportContext() {
        return new ReportContext();
    }
}
