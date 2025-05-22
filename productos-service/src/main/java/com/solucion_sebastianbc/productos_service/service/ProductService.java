package com.solucion_sebastianbc.productos_service.service;

import com.solucion_sebastianbc.productos_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.productos_service.dto.JsonApiListResponseDTO;
import com.solucion_sebastianbc.productos_service.dto.ProductAttributesDTO;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    JsonApiDataDTO<ProductAttributesDTO> crearProducto(ProductAttributesDTO productAttributesDTO);
    JsonApiDataDTO<ProductAttributesDTO> obtenerProductoPorId(Long id);
    JsonApiListResponseDTO<ProductAttributesDTO> obtenerTodosLosProductos(Pageable pageable);
    JsonApiDataDTO<ProductAttributesDTO> actualizarProducto(Long id, ProductAttributesDTO productAttributesDTO);
    void eliminarProducto(Long id);
}
