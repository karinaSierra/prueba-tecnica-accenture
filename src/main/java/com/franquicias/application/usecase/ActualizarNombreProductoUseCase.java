package com.franquicias.application.usecase;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.repository.ProductoRepository;
import reactor.core.publisher.Mono;

public class ActualizarNombreProductoUseCase {
    
    private final ProductoRepository productoRepository;

    public ActualizarNombreProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Mono<Producto> execute(Long productoId, String nuevoNombre) {
        if (productoId == null) {
            return Mono.error(new IllegalArgumentException("El id del producto no puede ser nulo"));
        }
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto no puede estar vacío"));
        }

        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto con id " + productoId + " no existe")))
                .flatMap(producto -> {
                    producto.setNombre(nuevoNombre.trim());
                    return productoRepository.update(producto);
                });
    }
}

