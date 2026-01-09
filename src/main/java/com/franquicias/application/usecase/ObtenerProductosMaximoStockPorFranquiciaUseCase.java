package com.franquicias.application.usecase;

import com.franquicias.domain.model.ProductoMaximoStock;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.ProductoRepository;
import reactor.core.publisher.Flux;

public class ObtenerProductosMaximoStockPorFranquiciaUseCase {
    
    private final ProductoRepository productoRepository;
    private final FranquiciaRepository franquiciaRepository;

    public ObtenerProductosMaximoStockPorFranquiciaUseCase(ProductoRepository productoRepository,
                                                           FranquiciaRepository franquiciaRepository) {
        this.productoRepository = productoRepository;
        this.franquiciaRepository = franquiciaRepository;
    }

    public Flux<ProductoMaximoStock> execute(Long franquiciaId) {
        if (franquiciaId == null) {
            return Flux.error(new IllegalArgumentException("El id de la franquicia no puede ser nulo"));
        }

        return franquiciaRepository.existsById(franquiciaId)
                .flatMapMany(exists -> {
                    if (!exists) {
                        return Flux.error(new IllegalArgumentException("La franquicia con id " + franquiciaId + " no existe"));
                    }
                    return productoRepository.findProductosMaximoStockByFranquiciaId(franquiciaId);
                });
    }
}

