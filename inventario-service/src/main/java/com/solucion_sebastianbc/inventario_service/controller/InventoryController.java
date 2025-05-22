package com.solucion_sebastianbc.inventario_service.controller;

import com.solucion_sebastianbc.inventario_service.dto.FullInventoryProductInfoDTO;
import com.solucion_sebastianbc.inventario_service.dto.InventoryAttributesDTO;
import com.solucion_sebastianbc.inventario_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.inventario_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventario")
@Validated
public class InventoryController {
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<JsonApiDataDTO<FullInventoryProductInfoDTO>> consultarInventarioPorProductoId(Long productoId) {
        JsonApiDataDTO<FullInventoryProductInfoDTO> inventarioInfo = inventoryService.consultarInventarioPorProductoId(productoId);
        return ResponseEntity.ok(inventarioInfo);
    }

    @PutMapping("/producto/{productoId}")
    public ResponseEntity<JsonApiDataDTO<InventoryAttributesDTO>> actualizarInventario(Long productoId, InventoryAttributesDTO inventoryAttributesDTO) {
        JsonApiDataDTO<InventoryAttributesDTO> inventarioActualizado = inventoryService.actualizarInventario(productoId, inventoryAttributesDTO);
        return ResponseEntity.ok(inventarioActualizado);
    }
}
