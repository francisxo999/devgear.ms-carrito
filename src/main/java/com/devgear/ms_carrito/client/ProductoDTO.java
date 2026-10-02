package com.devgear.ms_carrito.client;

import java.math.BigDecimal;

public record ProductoDTO(
    Long id,
    String nombre,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String categoria,
    String imagenUrl,
    Boolean activo
) {}