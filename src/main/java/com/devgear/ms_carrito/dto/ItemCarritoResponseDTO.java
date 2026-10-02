package com.devgear.ms_carrito.dto;

import java.math.BigDecimal;

public record ItemCarritoResponseDTO(
    Long id,
    Long productoId,
    String nombreProducto,
    BigDecimal precioUnitario,
    Integer cantidad,
    BigDecimal subtotal
) {}