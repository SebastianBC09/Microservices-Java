package com.solucion_sebastianbc.productos_service.service;

import com.solucion_sebastianbc.productos_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.productos_service.dto.JsonApiListResponseDTO;
import com.solucion_sebastianbc.productos_service.dto.ProductAttributesDTO;
import com.solucion_sebastianbc.productos_service.exception.ProductNotFoundException;
import com.solucion_sebastianbc.productos_service.model.Product;
import com.solucion_sebastianbc.productos_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StandardProductService implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public StandardProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public JsonApiDataDTO<ProductAttributesDTO> crearProducto(ProductAttributesDTO attributes) {
        Product producto = new Product();
        producto.setNombre(attributes.getNombre());
        producto.setPrecio(attributes.getPrecio());
        Product nuevoProducto = productRepository.save(producto);
        return convertToDTO(nuevoProducto);
    };

    @Override
    @Transactional
    public JsonApiDataDTO<ProductAttributesDTO> obtenerProductoPorId(Long id) {
        Product producto = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no encontrado"));
        return convertToDTO(producto);
    };

    @Override
    @Transactional
    public JsonApiListResponseDTO<ProductAttributesDTO> obtenerTodosLosProductos(Pageable pageable) {
        Page<Product> paginaProductos = productRepository.findAll(pageable);
        List<JsonApiDataDTO<ProductAttributesDTO>> productosDTOs = paginaProductos.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new JsonApiListResponseDTO<>(productosDTOs);
    };

    @Override
    @Transactional
    public JsonApiDataDTO<ProductAttributesDTO> actualizarProducto(Long id, ProductAttributesDTO attributes) {
        Product productoExistente = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Producto con ID " + id + " no encontrado para actualizar"));

        productoExistente.setNombre(attributes.getNombre());
        productoExistente.setPrecio(attributes.getPrecio());
        Product productoActualizado = productRepository.save(productoExistente);
        return convertToDTO(productoActualizado);
    };

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if(!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Producto con ID " + id + " no encontrado para eliminar");
        }
        productRepository.deleteById(id);
    };

    private JsonApiDataDTO<ProductAttributesDTO> convertToDTO(Product producto) {
        ProductAttributesDTO attributes = new ProductAttributesDTO(producto.getNombre(), producto.getPrecio());
        return new JsonApiDataDTO<>("productos", producto.getId().toString(), attributes);
    }

}
