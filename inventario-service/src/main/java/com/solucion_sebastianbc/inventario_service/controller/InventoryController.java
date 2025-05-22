package com.solucion_sebastianbc.inventario_service.controller;

import com.solucion_sebastianbc.inventario_service.dto.FullInventoryProductInfoDTO;
import com.solucion_sebastianbc.inventario_service.dto.InventoryAttributesDTO;
import com.solucion_sebastianbc.inventario_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.inventario_service.service.InventoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "API para la gestión de inventarios de productos")
@Validated
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Operation(summary = "Consultar cantidad de un producto y su información", description = "Obtiene la cantidad disponible de un producto específico por su ID, " + "junto con la información del producto obtenida del microservicio de productos.",responses = {
      @ApiResponse(responseCode = "200", description = "Información del inventario y producto recuperada",content = @Content(mediaType = "application/vnd.api+json",schema = @Schema(implementation = JsonApiDataDTO.class))), @ApiResponse(responseCode = "404", description = "Producto o inventario no encontrado (o error al obtener info del producto)"), @ApiResponse(responseCode = "500", description = "Error interno del servidor o al comunicarse con el servicio de productos")})
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<JsonApiDataDTO<FullInventoryProductInfoDTO>> consultarInventarioPorProductoId(
            @Parameter(description = "ID del producto a consultar en el inventario.", required = true, example = "1")
            @PathVariable Long productoId) {
        JsonApiDataDTO<FullInventoryProductInfoDTO> inventarioInfo = inventoryService.consultarInventarioPorProductoId(productoId);
        return ResponseEntity.ok(inventarioInfo);
    }

    @Operation(summary = "Actualizar cantidad de un producto en el inventario", description = "Actualiza la cantidad disponible de un producto específico. Si el producto no tiene entrada de inventario, se crea una.", responses = { @ApiResponse(responseCode = "200", description = "Inventario actualizado exitosamente", content = @Content(mediaType = "application/vnd.api+json", schema = @Schema(implementation = JsonApiDataDTO.class))), @ApiResponse(responseCode = "400", description = "Solicitud inválida (ej: cantidad negativa, datos faltantes)"), @ApiResponse(responseCode = "404", description = "Producto no encontrado (si se valida contra el servicio de productos y no existe)"), @ApiResponse(responseCode = "500", description = "Error interno del servidor")})
    @PutMapping("/producto/{productoId}")
    public ResponseEntity<JsonApiDataDTO<InventoryAttributesDTO>> actualizarInventario(
            @Parameter(description = "ID del producto cuyo inventario se actualizará.", required = true, example = "1")
            @PathVariable Long productoId,
            @Valid @RequestBody InventoryAttributesDTO inventoryAttributesDTO) {
        JsonApiDataDTO<InventoryAttributesDTO> inventarioActualizado = inventoryService.actualizarInventario(productoId, inventoryAttributesDTO);
        return ResponseEntity.ok(inventarioActualizado);
    }
}
