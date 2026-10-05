package com.devgear.ms_carrito.repository;

import com.devgear.ms_carrito.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuarioId(String usuarioId);

    // Evita duplicar filas cuando el usuario agrega el mismo producto dos veces
    Optional<ItemCarrito> findByUsuarioIdAndProductoId(String usuarioId, Long productoId);

    // Vacía el carrito completo (lo usa ms-ordenes tras confirmar una compra)
    void deleteByUsuarioId(String usuarioId);
}