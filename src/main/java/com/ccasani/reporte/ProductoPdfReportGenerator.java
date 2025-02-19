package com.ccasani.reporte;

import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ReporteGerador;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
@Slf4j
@Component
public class ProductoPdfReportGenerator implements ReporteGerador {


    @Override
    public InputStreamResource generateReport(List<ProductoResponse> productos) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Customer Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(7);
            table.addCell("ID");
            table.addCell("Categoria");
            table.addCell("Nombre");
            table.addCell("Descripción");
            table.addCell("Precio Compra");
            table.addCell("Precio Venta");
            table.addCell("Stock");

            for (ProductoResponse producto: productos) {
                table.addCell(String.valueOf(producto.getIdProducto()));
                table.addCell(producto.getCategoria().getNombreCategoria());
                table.addCell(producto.getNombreProducto());
                table.addCell(producto.getDescripcion());
                table.addCell(String.valueOf(producto.getPrecioCompra()));
                table.addCell(String.valueOf(producto.getPrecioVenta()));
                table.addCell(String.valueOf(producto.getStockActual()));

            }

            document.add(table);
            document.close();

            return new InputStreamResource(new ByteArrayInputStream(out.toByteArray()));

        } catch (Exception e) {
            log.error("Error generating PDF report: {}", e.getMessage());
            throw new RuntimeException("Unable to generate PDF report");
        }
    }
}
