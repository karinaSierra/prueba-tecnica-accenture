package com.franquicias.domain.repository;

import com.franquicias.domain.model.Sucursal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SucursalRepository {
    Mono<Sucursal> save(Sucursal sucursal);
    Mono<Sucursal> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Flux<Sucursal> findByFranquiciaId(Long franquiciaId);
    Mono<Sucursal> update(Sucursal sucursal);
}

