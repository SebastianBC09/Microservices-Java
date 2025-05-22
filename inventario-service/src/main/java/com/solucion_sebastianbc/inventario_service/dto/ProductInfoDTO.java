package com.solucion_sebastianbc.inventario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfoDTO {
    private String nombre;
    private BigDecimal precio;
}
