package com.franquicias.domain.repository;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.model.ProductoMaximoStock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoRepository {
    Mono<Producto> save(Producto producto);
    Mono<Producto> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Flux<Producto> findBySucursalId(Long sucursalId);
    Mono<Void> deleteById(Long id);
    Mono<Producto> update(Producto producto);
    Flux<ProductoMaximoStock> findProductosMaximoStockByFranquiciaId(Long franquiciaId);
}

