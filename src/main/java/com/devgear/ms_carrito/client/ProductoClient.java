package com.devgear.ms_carrito.client;

import com.devgear.ms_carrito.exception.ResourceNotFoundException;
import com.devgear.ms_carrito.exception.ServicioProductosException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ProductoClient {

    private final RestClient restClient;

    public ProductoClient(RestClient productosRestClient) {
        this.restClient = productosRestClient;
    }

    public ProductoDTO obtenerProducto(Long productoId, String bearerToken) {
        try {
            return restClient.get()
                    .uri("/api/v1/productos/{id}", productoId)
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .retrieve()
                    .body(ProductoDTO.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException("El producto " + productoId + " no existe");
        } catch (RestClientException ex) {
            throw new ServicioProductosException("No se pudo validar el producto con ms-productos: " + ex.getMessage());
        }
    }
}