package com.solucion_sebastianbc.productos_service.controller;

import com.solucion_sebastianbc.productos_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.productos_service.dto.JsonApiListResponseDTO;
import com.solucion_sebastianbc.productos_service.dto.ProductAttributesDTO;
import com.solucion_sebastianbc.productos_service.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

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
@Tag(name = "Productos", description = "API para la gestión de productos") // Tag para Swagger
@Validated
public class ProductoController {

    private final ProductService productService;

    @Autowired
    public ProductoController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Crear un nuevo producto",description = "Crea un nuevo producto con la información proporcionada.", responses = { @ApiResponse(responseCode = "201", description = "Producto creado exitosamente", content = @Content(mediaType = "application/vnd.api+json", schema = @Schema(implementation = JsonApiDataDTO.class))),@ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: datos faltantes o incorrectos)")})
    @PostMapping
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> crearProducto(
            @Valid @RequestBody ProductAttributesDTO productAttributesDTO) {
        JsonApiDataDTO<ProductAttributesDTO> nuevoProductoDTO = productService.crearProducto(productAttributesDTO);
        return new ResponseEntity<>(nuevoProductoDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener un producto por ID",description = "Recupera los detalles de un producto específico utilizando su ID.", responses = { @ApiResponse(responseCode = "200", description = "Producto encontrado", content = @Content(mediaType = "application/vnd.api+json", schema = @Schema(implementation = JsonApiDataDTO.class))), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> obtenerProductoPorId(
            @Parameter(description = "ID del producto a obtener.", required = true, example = "1")
            @PathVariable Long id) {
        JsonApiDataDTO<ProductAttributesDTO> productoDTO = productService.obtenerProductoPorId(id);
        return ResponseEntity.ok(productoDTO);
    }

    @Operation(summary = "Listar todos los productos con paginación",description = "Obtiene una lista paginada de todos los productos disponibles.",responses = { @ApiResponse(responseCode = "200", description = "Lista de productos recuperada",content = @Content(mediaType = "application/vnd.api+json", schema = @Schema(implementation = JsonApiListResponseDTO.class)))})
    @GetMapping
    public ResponseEntity<JsonApiListResponseDTO<ProductAttributesDTO>> obtenerTodosLosProductos(
            @Parameter(description = "Número de página (basado en cero) a obtener.", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página.", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        JsonApiListResponseDTO<ProductAttributesDTO> productosDTO = productService.obtenerTodosLosProductos(pageable);
        return ResponseEntity.ok(productosDTO);
    }

    @Operation(summary = "Actualizar un producto existente",description = "Actualiza los detalles de un producto existente basado en su ID.",responses = { @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente", content = @Content(mediaType = "application/vnd.api+json", schema = @Schema(implementation = JsonApiDataDTO.class))), @ApiResponse(responseCode = "400", description = "Solicitud inválida"),@ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<JsonApiDataDTO<ProductAttributesDTO>> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar.", required = true, example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProductAttributesDTO productAttributesDTO) {
        JsonApiDataDTO<ProductAttributesDTO> productoActualizadoDTO = productService.actualizarProducto(id, productAttributesDTO);
        return ResponseEntity.ok(productoActualizadoDTO);
    }

    @Operation(summary = "Eliminar un producto por ID", description = "Elimina un producto de la base de datos utilizando su ID.", responses = { @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente (Sin contenido)"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar.", required = true, example = "1")
            @PathVariable Long id) {
        productService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
}
