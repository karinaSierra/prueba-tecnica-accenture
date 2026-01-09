package com.franquicias.application.usecase;

import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.SucursalRepository;
import reactor.core.publisher.Mono;

public class AgregarSucursalUseCase {
    
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;

    public AgregarSucursalUseCase(SucursalRepository sucursalRepository, 
                                  FranquiciaRepository franquiciaRepository) {
        this.sucursalRepository = sucursalRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public Mono<Sucursal> execute(Long franquiciaId, String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío"));
        }

        return franquiciaRepository.existsById(franquiciaId)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new IllegalArgumentException("La franquicia con id " + franquiciaId + " no existe"));
                    }
                    Sucursal sucursal = new Sucursal(null, nombre.trim(), franquiciaId);
                    return sucursalRepository.save(sucursal);
                });
    }
}

