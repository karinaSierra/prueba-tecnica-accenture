package com.franquicias.infrastructure.persistence.r2dbc.mapper;

import com.franquicias.domain.model.Franquicia;
import com.franquicias.infrastructure.persistence.r2dbc.entity.FranquiciaEntity;

public class FranquiciaMapper {
    
    public static Franquicia toDomain(FranquiciaEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Franquicia(
            entity.getId(),
            entity.getNombre(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public static FranquiciaEntity toEntity(Franquicia domain) {
        if (domain == null) {
            return null;
        }
        return new FranquiciaEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }
}

