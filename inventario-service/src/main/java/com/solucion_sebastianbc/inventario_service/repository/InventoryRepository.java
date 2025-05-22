package com.solucion_sebastianbc.inventario_service.repository;

import com.solucion_sebastianbc.inventario_service.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository <Inventory, Long> {
    Optional<Inventory> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
}
