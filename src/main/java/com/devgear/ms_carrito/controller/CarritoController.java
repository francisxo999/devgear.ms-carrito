package com.devgear.ms_carrito.controller;

import com.devgear.ms_carrito.model.ItemCarrito;
import com.devgear.ms_carrito.repository.ItemCarritoRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final ItemCarritoRepository carritoRepository;

    public CarritoController(ItemCarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @GetMapping
    public List<ItemCarrito> verMiCarrito(JwtAuthenticationToken auth) {
        // Extrae el ID único del usuario desde el token de Azure (claim "oid" o "sub")
        String usuarioId = auth.getToken().getClaimAsString("oid");
        return carritoRepository.findByUsuarioId(usuarioId);
    }

    @PostMapping
    public ItemCarrito agregarAlCarrito(@RequestBody ItemCarrito item, JwtAuthenticationToken auth) {
        String usuarioId = auth.getToken().getClaimAsString("oid");
        item.setUsuarioId(usuarioId);
        return carritoRepository.save(item);
    }
}