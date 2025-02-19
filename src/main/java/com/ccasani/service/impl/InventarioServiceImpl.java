package com.ccasani.service.impl;

import com.ccasani.exception.ApiException;
import com.ccasani.mapper.ProductoMapper;
import com.ccasani.model.entity.Categoria;
import com.ccasani.model.entity.Producto;
import com.ccasani.model.enumeration.TipoMovimiento;
import com.ccasani.model.request.ProductoRequest;
import com.ccasani.model.response.CategoriaResponse;
import com.ccasani.model.response.DataResponse;
import com.ccasani.model.response.MovimientoInventarioResponse;
import com.ccasani.model.response.ProductoResponse;
import com.ccasani.repository.CategoriaRepository;
import com.ccasani.repository.MovimientoInventarioRepository;
import com.ccasani.repository.ProductoRepository;
import com.ccasani.service.inf.InventarioService;
import com.ccasani.service.specification.ProductoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
public class InventarioServiceImpl implements InventarioService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Override
    public List<CategoriaResponse> listarCategorias() {

        return this.categoriaRepository.findAll().stream()
                .map(ProductoMapper::mapToCategoriaResponse).toList();
    }

    @Override
    public CategoriaResponse buscarCategoriaPorId(Integer idCategoria) {
        return ProductoMapper.mapToCategoriaResponse(this.buscarCategoriaEntityPorId(idCategoria));
    }

    @Override
    public DataResponse productosPaginado(String buscarProducto, Integer numeroPagina, Integer tamanioPagina) {
        Pageable pageable = PageRequest.of(numeroPagina, tamanioPagina);
        Specification<Producto> productoSpecification = ProductoSpecification.hasProducto(buscarProducto);
        Specification<Producto> specification = Specification.where(productoSpecification);

        Page<Producto> productos = this.productoRepository.findAll(specification, pageable);
        return DataResponse.builder().producto(productos.stream().map(ProductoMapper::mapToProductoResponse).toList())
                .total(this.productoRepository.findAll(specification).size())
                .build();
    }

    @Override
    public List<ProductoResponse> listarProductos() {
        List<Producto> productoList = this.productoRepository.findAll();
        return productoList.stream()
                .map(ProductoMapper::mapToProductoResponse).toList();
    }

    @Override
    public ProductoResponse buscarProductoPorId(Integer idProducto) {
        return ProductoMapper.mapToProductoResponse(this.buscarProductoEntityPorId(idProducto));
    }

    @Transactional
    @Override
    public void agregarProducto(ProductoRequest request) {


        Producto productoResponse = this.productoRepository.save(ProductoMapper.mapToProducto(request, this.buscarCategoriaPorId(request.getIdCategoria())));

        this.movimientoInventarioRepository.save(ProductoMapper.mapToMovimientoInventario(productoResponse, request.getStockActual(), TipoMovimiento.ENTRADA));

    }

    @Transactional
    @Override
    public void actualizarProducto(Integer idProducto, ProductoRequest request) {
        Producto producto = this.buscarProductoEntityPorId(idProducto);
        producto.setNombreProducto(request.getNombreProducto());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStockActual(producto.getStockActual() + request.getStockActual());
        producto.setCategoria(this.buscarCategoriaEntityPorId(request.getIdCategoria()));
        Producto productoActualizado = this.productoRepository.save(producto);
        this.movimientoInventarioRepository.save(ProductoMapper.mapToMovimientoInventario(productoActualizado, request.getStockActual(), TipoMovimiento.MODIFICADO));

    }

    @Override
    public void eliminarProducto(Integer idProducto) {
        ProductoResponse productoResponse = this.buscarProductoPorId(idProducto);
        //this.movimientoInventarioRepository.deleteByIdProducto(idProducto);
        this.productoRepository.deleteById(productoResponse.getIdProducto());
    }



    @Override
    public List<MovimientoInventarioResponse> listarMovimientos() {
        return this.movimientoInventarioRepository.findAll().stream()
                .map(ProductoMapper::mapToMovimientoInventarioResponse).toList();
    }


    private Producto buscarProductoEntityPorId(Integer id) {
        return this.productoRepository.findById(id)
                .orElseThrow(() -> new ApiException("Producto no encontrado", NOT_FOUND.value(), NOT_FOUND));

    }

    private Categoria buscarCategoriaEntityPorId(Integer id) {
        return this.categoriaRepository.findById(id)
                .orElseThrow(() -> new ApiException("Categoría no encontrado", NOT_FOUND.value(), NOT_FOUND));


    }

}
