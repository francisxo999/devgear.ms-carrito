package com.devgear.ms_carrito.controller;

import com.devgear.ms_carrito.dto.ItemCarritoRequestDTO;
import com.devgear.ms_carrito.dto.ItemCarritoResponseDTO;
import com.devgear.ms_carrito.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<List<ItemCarritoResponseDTO>> verMiCarrito(JwtAuthenticationToken auth) {
        return ResponseEntity.ok(carritoService.verCarrito(usuarioId(auth), bearerToken(auth)));
    }

    @PostMapping
    public ResponseEntity<ItemCarritoResponseDTO> agregarAlCarrito(
            @Valid @RequestBody ItemCarritoRequestDTO request, JwtAuthenticationToken auth) {
        ItemCarritoResponseDTO item = carritoService.agregarAlCarrito(usuarioId(auth), request, bearerToken(auth));
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<ItemCarritoResponseDTO> actualizarCantidad(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemCarritoRequestDTO request,
            JwtAuthenticationToken auth) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(usuarioId(auth), itemId, request, bearerToken(auth)));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long itemId, JwtAuthenticationToken auth) {
        carritoService.eliminarItem(usuarioId(auth), itemId);
        return ResponseEntity.noContent().build();
    }

    // Vacía el carrito completo. Lo llama ms-ordenes justo después de crear la orden.
    @DeleteMapping
    public ResponseEntity<Void> vaciarCarrito(JwtAuthenticationToken auth) {
        carritoService.vaciarCarrito(usuarioId(auth));
        return ResponseEntity.noContent().build();
    }

    // Extrae el ID único del usuario desde el token de Azure (claim "oid")
    private String usuarioId(JwtAuthenticationToken auth) {
        return auth.getToken().getClaimAsString("oid");
    }

    // Reenviamos el mismo token a ms-productos para consultarlo
    private String bearerToken(JwtAuthenticationToken auth) {
        return "Bearer " + auth.getToken().getTokenValue();
    }
}