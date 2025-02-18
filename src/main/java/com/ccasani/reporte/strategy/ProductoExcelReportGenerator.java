package com.ccasani.reporte.strategy;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ReporteGerador;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
@Slf4j
public class ProductoExcelReportGenerator implements ReporteGerador {

    private final List<ProductoResponse> productoResponses;

    public ProductoExcelReportGenerator(List<ProductoResponse> productoResponses) {
        this.productoResponses = productoResponses;
    }


    @Override
    public InputStreamResource generateReport() {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Producto");
            String[] headers = { "ID","Categoría", "Nombre", "Descripción",  "Precio Compra", "Precio Venta", "Stock" };

            // Crear fila de encabezado
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Agregar datos
            int rowIndex = 1;
            for(ProductoResponse producto: productoResponses) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(producto.getIdProducto());
                row.createCell(1).setCellValue(producto.getCategoria().getNombreCategoria());
                row.createCell(2).setCellValue(producto.getNombreProducto());
                row.createCell(3).setCellValue(producto.getDescripcion());
                row.createCell(4).setCellValue(producto.getPrecioCompra().doubleValue());
                row.createCell(5).setCellValue(producto.getPrecioVenta().doubleValue());
                row.createCell(6).setCellValue(producto.getStockActual());

            }

            workbook.write(out);
            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            log.error("Error generating Excel report: {}", e.getMessage());
            throw new RuntimeException("Unable to generate Excel report");
        }
    }
}
