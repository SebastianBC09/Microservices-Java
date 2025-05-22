package com.solucion_sebastianbc.productos_service.controller;

import com.solucion_sebastianbc.productos_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.productos_service.dto.JsonApiListResponseDTO;
import com.solucion_sebastianbc.productos_service.dto.ProductAttributesDTO;
import com.solucion_sebastianbc.productos_service.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
public class ProductoController {

    private final ProductService productService;

    @Autowired
    public ProductoController(ProductService productService) {
        this.productService = productService;
    };


    @PostMapping
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> crearProducto(
            @Valid @RequestBody ProductAttributesDTO ProductAttributesDTO) {
        JsonApiDataDTO<ProductAttributesDTO> nuevoProductoDTO = productService.crearProducto(ProductAttributesDTO);
        return new ResponseEntity<>(nuevoProductoDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> obtenerProductoPorId(@PathVariable Long id) {
        JsonApiDataDTO<ProductAttributesDTO> productoDTO = productService.obtenerProductoPorId(id);
        return ResponseEntity.ok(productoDTO);
    }

    @GetMapping
    public ResponseEntity<JsonApiListResponseDTO<ProductAttributesDTO>> obtenerTodosLosProductos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        JsonApiListResponseDTO<ProductAttributesDTO> productosDTO = productService.obtenerTodosLosProductos(pageable);
        return ResponseEntity.ok(productosDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> actualizarProducto(
            @PathVariable Long id,
            @Valid @RequestBody ProductAttributesDTO ProductAttributesDTO) {
        JsonApiDataDTO<ProductAttributesDTO> productoActualizadoDTO = productService.actualizarProducto(id, ProductAttributesDTO);
        return ResponseEntity.ok(productoActualizadoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

}
