package com.franquicias.application.usecase;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.repository.ProductoRepository;
import reactor.core.publisher.Mono;

public class ModificarStockProductoUseCase {
    
    private final ProductoRepository productoRepository;

    public ModificarStockProductoUseCase(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public Mono<Producto> execute(Long productoId, Integer nuevoStock) {
        if (productoId == null) {
            return Mono.error(new IllegalArgumentException("El id del producto no puede ser nulo"));
        }
        if (nuevoStock == null || nuevoStock < 0) {
            return Mono.error(new IllegalArgumentException("El stock debe ser un número positivo o cero"));
        }

        return productoRepository.findById(productoId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("El producto con id " + productoId + " no existe")))
                .flatMap(producto -> {
                    producto.setStock(nuevoStock);
                    return productoRepository.update(producto);
                });
    }
}

