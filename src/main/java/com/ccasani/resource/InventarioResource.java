package com.ccasani.resource;

import com.ccasani.model.request.ProductoRequest;
import com.ccasani.model.response.CustomHttpResponse;
import com.ccasani.model.response.DataResponse;
import com.ccasani.model.response.HttpResponse;
import com.ccasani.model.response.ProductoResponse;
import com.ccasani.reporte.ProductoReporte;
import com.ccasani.service.inf.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventario")
public class InventarioResource {
    private final InventarioService inventarioService;

    @GetMapping("/productos")
    public ResponseEntity<CustomHttpResponse> listarProductos(@RequestParam String producto, @RequestParam Integer numeroPagina, @RequestParam Integer tamanioPagina) {
        DataResponse responseList = this.inventarioService.productosPaginado(producto, numeroPagina, tamanioPagina);
        return ResponseEntity.ok()
                .body(CustomHttpResponse
                        .builder()
                        .fecha(LocalDateTime.now())
                        .status(OK)
                        .statusCode(OK.value())
                        .mensaje("Lista de productos")
                        .data(responseList)
                        .build());
    }

    @PostMapping("/registro")
    public ResponseEntity<HttpResponse> agregarProducto(@Valid @RequestBody ProductoRequest request) {
        this.inventarioService.agregarProducto(request);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(CREATED)
                .statusCode(CREATED.value())
                .mensaje("Producto creado")
                .build());
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<HttpResponse> actualizarProducto(@PathVariable Integer idProducto, @RequestBody ProductoRequest request) {
        this.inventarioService.actualizarProducto(idProducto, request);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(OK)
                .statusCode(OK.value())
                .mensaje("Producto actualizado correctamente.")
                .build());
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<HttpResponse> actualizarProducto(@PathVariable Integer idProducto) {
        this.inventarioService.eliminarProducto(idProducto);
        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(NO_CONTENT)
                .statusCode(NO_CONTENT.value())
                .mensaje("Eliminación exitoso")
                .build());
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<HttpResponse> buscarPorId(@PathVariable Integer idProducto) {

        return ResponseEntity.created(this.getUri()).body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(NO_CONTENT)
                .statusCode(NO_CONTENT.value())
                .mensaje("Eliminación exitoso")
                        .data(Map.of("producto", this.inventarioService.buscarProductoPorId(idProducto)))
                .build());
    }

    @GetMapping("/categoria")
    public ResponseEntity<HttpResponse> obtenerCategorias() {
        return ResponseEntity.ok().body(HttpResponse
                .builder()
                .fecha(LocalDateTime.now())
                .status(OK)
                .statusCode(OK.value())
                .mensaje("Lista categoria.")
                .data(Map.of("categoria", this.inventarioService.listarCategorias()))
                .build());
    }

    @GetMapping("/producto/download/reporte")
    public ResponseEntity<Resource> downloadReport(@RequestParam String producto, @RequestParam Integer numeroPagina, @RequestParam Integer tamanioPagina) {
        List<ProductoResponse> productoResponseList = new ArrayList<>();
        this.inventarioService.productosPaginado(producto, numeroPagina, tamanioPagina).getProducto().iterator().forEachRemaining(productoResponseList::add);

        ProductoReporte report = new ProductoReporte(productoResponseList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("File-Name", "producto-reporte.xlsx");
        headers.add(CONTENT_DISPOSITION, "attachment;File-Name=producto-reporte.xlsx");
        return ResponseEntity.ok().contentType(parseMediaType("application/vnd.ms-excel"))
                .headers(headers).body(report.export());
    }

    private URI getUri() {
        return URI.create(fromCurrentContextPath().path("/api/inventario").toUriString());
    }
}
