package com.ccasani.reporte;

import com.ccasani.exception.ApiException;
import com.ccasani.model.response.ProductoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;

import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.time.DateFormatUtils.format;

@Slf4j
public class ProductoReporte {
    public static final String DATE_FORMATTER = "yyyy-MM-dd hh:mm:ss";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ProductoResponse> productos;
    private static String[] HEADERS = { "ID","Categoría", "Nombre", "Descripción",  "Precio Compra", "Precio Venta", "Stock" };

    public ProductoReporte(List<ProductoResponse> productos) {
        this.productos = productos;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Producto");
        setHeaders();
    }

    private void setHeaders() {
        Row headerRow = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(14);
        style.setFont(font);
        range(0, HEADERS.length).forEach(index -> {
            Cell cell = headerRow.createCell(index);
            cell.setCellValue(HEADERS[index]);
            cell.setCellStyle(style);
        });
    }

    public InputStreamResource export() {
        return generateReport();
    }

    private InputStreamResource generateReport() {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(10);
            style.setFont(font);
            int rowIndex = 1;
            for(ProductoResponse producto: productos) {
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
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new ApiException("Error reporte",HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
