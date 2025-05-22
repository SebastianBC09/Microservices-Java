package com.solucion_sebastianbc.inventario_service.service;

import com.solucion_sebastianbc.inventario_service.dto.FullInventoryProductInfoDTO;
import com.solucion_sebastianbc.inventario_service.dto.InventoryAttributesDTO;
import com.solucion_sebastianbc.inventario_service.dto.JsonApiDataDTO;

public interface InventoryService {
    JsonApiDataDTO<FullInventoryProductInfoDTO> consultarInventarioPorProductoId(Long productoId);
    JsonApiDataDTO<InventoryAttributesDTO> actualizarInventario(Long productoId, InventoryAttributesDTO attributesDTO);
}
