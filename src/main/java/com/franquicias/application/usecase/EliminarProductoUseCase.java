package com.franquicias.application.usecase;

import com.franquicias.domain.repository.ProductoRepository;
import reactor.core.publisher.Mono;

public class EliminarProductoUseCase {
    
    private final ProductoRepository productoRepository;

    public EliminarProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Mono<Void> execute(Long productoId) {
        if (productoId == null) {
            return Mono.error(new IllegalArgumentException("El id del producto no puede ser nulo"));
        }

        return productoRepository.existsById(productoId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("El producto con id " + productoId + " no existe"));
                    }
                    return productoRepository.deleteById(productoId);
                });
    }
}

