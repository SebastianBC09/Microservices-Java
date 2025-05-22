package com.solucion_sebastianbc.productos_service.repository;

import com.solucion_sebastianbc.productos_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
