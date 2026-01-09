package com.franquicias.application.usecase;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.repository.ProductoRepository;
import com.franquicias.domain.repository.SucursalRepository;
import reactor.core.publisher.Mono;

public class AgregarProductoUseCase {
    
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    public AgregarProductoUseCase(ProductoRepository productoRepository, 
                                  SucursalRepository sucursalRepository) {
        this.productoRepository = productoRepository;
        this.sucursalRepository = sucursalRepository;
    }

    public Mono<Producto> execute(Long sucursalId, String nombre, Integer stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre del producto no puede estar vacío"));
        }
        if (stock == null || stock < 0) {
            return Mono.error(new IllegalArgumentException("El stock debe ser un número positivo o cero"));
        }

        return sucursalRepository.existsById(sucursalId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("La sucursal con id " + sucursalId + " no existe"));
                    }
                    Producto producto = new Producto(null, nombre.trim(), stock, sucursalId);
                    return productoRepository.save(producto);
                });
    }
}

