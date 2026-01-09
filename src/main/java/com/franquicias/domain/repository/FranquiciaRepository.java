package com.franquicias.domain.repository;

import com.franquicias.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    Mono<Franquicia> save(Franquicia franquicia);
    Mono<Franquicia> findById(Long id);
    Mono<Boolean> existsById(Long id);
    Mono<Franquicia> update(Franquicia franquicia);
}

