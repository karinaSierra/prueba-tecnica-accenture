package com.franquicias.adapters.presentation.mapper;

import com.franquicias.adapters.presentation.dto.FranquiciaResponse;
import com.franquicias.domain.model.Franquicia;

public class FranquiciaMapper {
    
    public static FranquiciaResponse toResponse(Franquicia franquicia) {
        if (franquicia == null) {
            return null;
        }
        return new FranquiciaResponse(
            franquicia.getId(),
            franquicia.getNombre(),
            franquicia.getCreatedAt(),
            franquicia.getUpdatedAt()
        );
    }
}

