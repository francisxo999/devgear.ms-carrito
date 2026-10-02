package com.devgear.ms_carrito.service;

import com.devgear.ms_carrito.client.ProductoClient;
import com.devgear.ms_carrito.client.ProductoDTO;
import com.devgear.ms_carrito.dto.ItemCarritoRequestDTO;
import com.devgear.ms_carrito.dto.ItemCarritoResponseDTO;
import com.devgear.ms_carrito.exception.ResourceNotFoundException;
import com.devgear.ms_carrito.model.ItemCarrito;
import com.devgear.ms_carrito.repository.ItemCarritoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoService {

    private final ItemCarritoRepository carritoRepository;
    private final ProductoClient productoClient;

    public CarritoService(ItemCarritoRepository carritoRepository, ProductoClient productoClient) {
        this.carritoRepository = carritoRepository;
        this.productoClient = productoClient;
    }

    public List<ItemCarritoResponseDTO> verCarrito(String usuarioId, String bearerToken) {
        return carritoRepository.findByUsuarioId(usuarioId).stream()
                .map(item -> enriquecerConProducto(item, bearerToken))
                .toList();
    }

    public ItemCarritoResponseDTO agregarAlCarrito(String usuarioId, ItemCarritoRequestDTO request, String bearerToken) {
        ProductoDTO producto = productoClient.obtenerProducto(request.productoId(), bearerToken);

        if (Boolean.FALSE.equals(producto.activo())) {
            throw new ResourceNotFoundException("El producto " + request.productoId() + " no está disponible");
        }
        if (producto.stock() < request.cantidad()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para \"" + producto.nombre() + "\". Disponible: " + producto.stock());
        }

        ItemCarrito item = carritoRepository.findByUsuarioIdAndProductoId(usuarioId, request.productoId())
                .map(existente -> {
                    int nuevaCantidad = existente.getCantidad() + request.cantidad();
                    if (producto.stock() < nuevaCantidad) {
                        throw new IllegalArgumentException(
                                "Stock insuficiente para \"" + producto.nombre() + "\". Disponible: " + producto.stock());
                    }
                    existente.setCantidad(nuevaCantidad);
                    return existente;
                })
                .orElseGet(() -> {
                    ItemCarrito nuevo = new ItemCarrito();
                    nuevo.setUsuarioId(usuarioId);
                    nuevo.setProductoId(request.productoId());
                    nuevo.setCantidad(request.cantidad());
                    return nuevo;
                });

        ItemCarrito guardado = carritoRepository.save(item);
        return mapToDTO(guardado, producto);
    }

    public ItemCarritoResponseDTO actualizarCantidad(String usuarioId, Long itemId, ItemCarritoRequestDTO request, String bearerToken) {
        ItemCarrito item = obtenerItemDelUsuario(usuarioId, itemId);

        ProductoDTO producto = productoClient.obtenerProducto(item.getProductoId(), bearerToken);
        if (producto.stock() < request.cantidad()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para \"" + producto.nombre() + "\". Disponible: " + producto.stock());
        }

        item.setCantidad(request.cantidad());
        return mapToDTO(carritoRepository.save(item), producto);
    }

    public void eliminarItem(String usuarioId, Long itemId) {
        ItemCarrito item = obtenerItemDelUsuario(usuarioId, itemId);
        carritoRepository.delete(item);
    }

    private ItemCarrito obtenerItemDelUsuario(String usuarioId, Long itemId) {
        ItemCarrito item = carritoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item de carrito no encontrado: " + itemId));

        if (!item.getUsuarioId().equals(usuarioId)) {
            throw new ResourceNotFoundException("Item de carrito no encontrado: " + itemId);
        }
        return item;
    }

    private ItemCarritoResponseDTO enriquecerConProducto(ItemCarrito item, String bearerToken) {
        ProductoDTO producto = productoClient.obtenerProducto(item.getProductoId(), bearerToken);
        return mapToDTO(item, producto);
    }

    private ItemCarritoResponseDTO mapToDTO(ItemCarrito item, ProductoDTO producto) {
        return new ItemCarritoResponseDTO(
                item.getId(),
                item.getProductoId(),
                producto.nombre(),
                producto.precio(),
                item.getCantidad(),
                producto.precio().multiply(java.math.BigDecimal.valueOf(item.getCantidad()))
        );
    }
}