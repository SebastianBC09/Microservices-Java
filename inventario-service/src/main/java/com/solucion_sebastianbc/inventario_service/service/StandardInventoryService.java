package com.solucion_sebastianbc.inventario_service.service;

import com.solucion_sebastianbc.inventario_service.client.ProductServiceClient;
import com.solucion_sebastianbc.inventario_service.dto.FullInventoryProductInfoDTO;
import com.solucion_sebastianbc.inventario_service.dto.InventoryAttributesDTO;
import com.solucion_sebastianbc.inventario_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.inventario_service.dto.ProductInfoDTO;
import com.solucion_sebastianbc.inventario_service.exception.ProductServiceException;
import com.solucion_sebastianbc.inventario_service.model.Inventory;
import com.solucion_sebastianbc.inventario_service.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardInventoryService implements InventoryService{
    private static final Logger logger = LoggerFactory.getLogger(StandardInventoryService.class);

    private final InventoryRepository inventoryRepository;
    private final ProductServiceClient productServiceClient;

    @Autowired
    public StandardInventoryService(InventoryRepository inventoryRepository, ProductServiceClient productServiceClient) {
        this.inventoryRepository = inventoryRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    @Transactional
    public JsonApiDataDTO<FullInventoryProductInfoDTO> consultarInventarioPorProductoId(Long productoId) {
        logger.info("Consultando inventario para producto ID: {}", productoId);

        ProductInfoDTO productoInfo = productServiceClient.getProductoInfo(productoId);
        if(productoInfo == null) {
            logger.warn("No se pudo obtener información del producto con ID: {} desde ProductosService.", productoId);
            throw new ProductServiceException("No se pudo obtener información para el producto ID: " + productoId + " desde el servicio de productos.");
        };

        Inventory inventario = inventoryRepository.findByProductoId(productoId)
                .orElseGet(() -> {
                    logger.info("No se encontró inventario para producto ID: {}. Asumiendo cantidad 0.", productoId);
                    Inventory nuevoInventario = new Inventory();
                    nuevoInventario.setProductoId(productoId);
                    nuevoInventario.setCantidad(0);
                            // No lo guardamos en la consulta, solo para devolver la info.
                            // Si la política fuera crearla, aquí iría un save.
                    return nuevoInventario;
                });

        FullInventoryProductInfoDTO combinedInfo = new FullInventoryProductInfoDTO(
                productoInfo.getNombre(),
                productoInfo.getPrecio(),
                inventario.getCantidad()
        );

        return new JsonApiDataDTO<>("inventarioProducto", productoId.toString(), combinedInfo);
    }

    @Override
    @Transactional
    public JsonApiDataDTO<InventoryAttributesDTO> actualizarInventario(Long productoId, InventoryAttributesDTO attributesDTO) {
        logger.info("Actualizando inventario para producto ID: {} con cantidad: {}", productoId, attributesDTO.getCantidad());

        Inventory inventario = inventoryRepository.findByProductoId(productoId)
                .orElseGet(() -> {
                    logger.info("Creando nueva entrada de inventario para producto ID: {}", productoId);
                    Inventory nuevoInventario = new Inventory();
                    nuevoInventario.setProductoId(productoId);
                    return nuevoInventario;
                });

        inventario.setCantidad(attributesDTO.getCantidad());
        Inventory inventarioActualizado = inventoryRepository.save(inventario);

        System.out.println("EVENTO: Inventario cambiado para producto_id: " + productoId + ", nueva cantidad: " + inventarioActualizado.getCantidad());
        logger.info("EVENTO: Inventario cambiado para producto_id: {}, nueva cantidad: {}", productoId, inventarioActualizado.getCantidad());

        InventoryAttributesDTO responseAttributes = new InventoryAttributesDTO(inventarioActualizado.getCantidad());
        return new JsonApiDataDTO<>("inventarios", inventarioActualizado.getProductoId().toString(), responseAttributes);
    }
}
