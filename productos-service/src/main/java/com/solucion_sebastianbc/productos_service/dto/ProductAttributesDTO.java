package com.solucion_sebastianbc.productos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributesDTO {
    private String nombre;
    private BigDecimal precio;
}
