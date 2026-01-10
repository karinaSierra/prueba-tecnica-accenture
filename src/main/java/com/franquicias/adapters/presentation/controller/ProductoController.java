package com.franquicias.adapters.presentation.controller;
import com.franquicias.adapters.presentation.dto.ActualizarNombreRequest;
import com.franquicias.adapters.presentation.dto.ModificarStockRequest;
import com.franquicias.adapters.presentation.dto.ProductoRequest;
import com.franquicias.adapters.presentation.dto.ProductoResponse;
import com.franquicias.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/sucursales/{sucursalId}/productos")
public class ProductoController {
    
    private final AgregarProductoUseCase agregarProductoUseCase;
    private final EliminarProductoUseCase eliminarProductoUseCase;
    private final ModificarStockProductoUseCase modificarStockProductoUseCase;
    private final ActualizarNombreProductoUseCase actualizarNombreProductoUseCase;

    public ProductoController(AgregarProductoUseCase agregarProductoUseCase,
                             EliminarProductoUseCase eliminarProductoUseCase,
                             ModificarStockProductoUseCase modificarStockProductoUseCase,
                             ActualizarNombreProductoUseCase actualizarNombreProductoUseCase) {
        this.agregarProductoUseCase = agregarProductoUseCase;
        this.eliminarProductoUseCase = eliminarProductoUseCase;
        this.modificarStockProductoUseCase = modificarStockProductoUseCase;
        this.actualizarNombreProductoUseCase = actualizarNombreProductoUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<ProductoResponse>> agregarProducto(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ProductoRequest request) {
        return agregarProductoUseCase.execute(sucursalId, request.getNombre(), request.getStock())
                .map(producto -> new ProductoResponse(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getStock(),
                    producto.getSucursalId(),
                    producto.getCreatedAt(),
                    producto.getUpdatedAt()
                ))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @DeleteMapping("/{productoId}")
    public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable Long productoId) {
        return eliminarProductoUseCase.execute(productoId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @PutMapping("/{productoId}/stock")
    public Mono<ResponseEntity<ProductoResponse>> modificarStockProducto(
            @PathVariable Long productoId,
            @Valid @RequestBody ModificarStockRequest request) {
        return modificarStockProductoUseCase.execute(productoId, request.getStock())
                .map(producto -> new ProductoResponse(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getStock(),
                    producto.getSucursalId(),
                    producto.getCreatedAt(),
                    producto.getUpdatedAt()
                ))
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{productoId}/nombre")
    public Mono<ResponseEntity<ProductoResponse>> actualizarNombreProducto(
            @PathVariable Long productoId,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreProductoUseCase.execute(productoId, request.getNombre())
                .map(producto -> new ProductoResponse(
                    producto.getId(),
                    producto.getNombre(),
                    producto.getStock(),
                    producto.getSucursalId(),
                    producto.getCreatedAt(),
                    producto.getUpdatedAt()
                ))
                .map(ResponseEntity::ok);
    }
}

