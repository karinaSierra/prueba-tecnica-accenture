package com.franquicias.application.usecase;

import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.SucursalRepository;
import reactor.core.publisher.Mono;

public class ActualizarNombreSucursalUseCase {
    
    private final SucursalRepository sucursalRepository;

    public ActualizarNombreSucursalUseCase(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    public Mono<Sucursal> execute(Long sucursalId, String nuevoNombre) {
        if (sucursalId == null) {
            return Mono.error(new IllegalArgumentException("El id de la sucursal no puede ser nulo"));
        }
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El nombre de la sucursal no puede estar vacío"));
        }

        return sucursalRepository.findById(sucursalId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("La sucursal con id " + sucursalId + " no existe")))
                .flatMap(sucursal -> {
                    sucursal.setNombre(nuevoNombre.trim());
                    return sucursalRepository.update(sucursal);
                });
    }
}

