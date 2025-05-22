package com.solucion_sebastianbc.productos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonApiListResponseDTO<T>{
    private List<JsonApiDataDTO<T>> data;
}
