package com.franquicias.application.usecase;

import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import reactor.core.publisher.Mono;

public class ActualizarNombreFranquiciaUseCase {
    
    private final FranquiciaRepository franquiciaRepository;

    public ActualizarNombreFranquiciaUseCase(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    public Mono<Franquicia> execute(Long franquiciaId, String nuevoNombre) {
        if (franquiciaId == null) {
            return Mono.error(new IllegalArgumentException("El id de la franquicia no puede ser nulo"));
        }
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia no puede estar vacío"));
        }

        return franquiciaRepository.findById(franquiciaId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La franquicia con id " + franquiciaId + " no existe")))
                .flatMap(franquicia -> {
                    franquicia.setNombre(nuevoNombre.trim());
                    return franquiciaRepository.update(franquicia);
                });
    }
}

