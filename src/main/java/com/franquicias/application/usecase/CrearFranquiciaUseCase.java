package com.franquicias.application.usecase;

import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;

public class CrearFranquiciaUseCase {
    
    private final FranquiciaRepository franquiciaRepository;

    public CrearFranquiciaUseCase(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    public Mono<Franquicia> execute(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia no puede estar vacío"));
        }

        Franquicia franquicia = new Franquicia(null, nombre.trim());
        return franquiciaRepository.save(franquicia);
    }
}

