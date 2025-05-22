package com.solucion_sebastianbc.inventario_service.client;

import com.solucion_sebastianbc.inventario_service.dto.JsonApiDataDTO;
import com.solucion_sebastianbc.inventario_service.dto.ProductInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Map;

@Component
public class ProductServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);
    private final RestTemplate restTemplate;

    @Value("${product-service.url}")
    private String productosServiceUrl;

    @Value("${product-service.apy-key}")
    private String productosServiceApiKey;

    @Autowired
    public ProductServiceClient(
            RestTemplateBuilder restTemplateBuilder,
            @Value("${product-service.connect-timeout:5}") int connectTimeout,
            @Value("${product-service.read-timeout:5}") int readTimeout
    ) {
        this.restTemplate = restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(connectTimeout))
                .readTimeout(Duration.ofSeconds(readTimeout))
                .build();
    }

    public ProductInfoDTO getProductoInfo(Long productoId) {
        String url = productosServiceUrl + '/' + productoId;
        logger.info("Llamando a Productos Service URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", productosServiceApiKey);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ParameterizedTypeReference<JsonApiDataDTO<Map<String, Object>>> responseType =
                    new ParameterizedTypeReference<>() {};

            ResponseEntity<JsonApiDataDTO<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    responseType
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getAttributes() != null) {
                return mapAttributesToProductoInfoDTO(response.getBody().getAttributes());
            } else {
                logger.warn("Respuesta no exitosa o cuerpo vacío de Productos Service para ID {}: {}", productoId, response.getStatusCode());
                return null;
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("Producto con ID {} no encontrado en Productos Service.", productoId);
                return null;
            }
            logger.error("Error de cliente al llamar a Productos Service para ID {}: {} - {}", productoId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Error de cliente al obtener información del producto: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            logger.error("Error de servidor al llamar a Productos Service para ID {}: {} - {}", productoId, e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("Error de servidor al obtener información del producto: " + e.getMessage(), e);
        } catch (ResourceAccessException e) {
            logger.error("Error de acceso/timeout al llamar a Productos Service para ID {}: {}", productoId, e.getMessage(), e);
            throw new RuntimeException("Error de comunicación (timeout/acceso) al obtener información del producto: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error inesperado al llamar a Productos Service para ID {}: {}", productoId, e.getMessage(), e);
            throw new RuntimeException("Error inesperado al obtener información del producto: " + e.getMessage(), e); // O una excepción personalizada
        }
    }

    private ProductInfoDTO mapAttributesToProductoInfoDTO(Map<String, Object> attributes) {
        ProductInfoDTO productoInfo = new ProductInfoDTO();
        productoInfo.setNombre((String) attributes.get("nombre"));

        Object precioObj = attributes.get("precio");
        if (precioObj instanceof Number) {
            productoInfo.setPrecio(new BigDecimal(precioObj.toString()));
        } else if (precioObj != null) {
            try {
                productoInfo.setPrecio(new BigDecimal(precioObj.toString()));
            } catch (NumberFormatException e) {
                logger.warn("No se pudo convertir el precio '{}' a BigDecimal.", precioObj, e);
            }
        }
        return productoInfo;
    }

}
