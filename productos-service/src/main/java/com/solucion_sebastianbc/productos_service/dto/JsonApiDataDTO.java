package com.solucion_sebastianbc.productos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonApiDataDTO<T> {
    private String type;
    private String id;
    private T attributes;
}
