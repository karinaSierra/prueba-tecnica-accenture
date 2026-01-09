package com.franquicias.infrastructure.persistence.r2dbc.mapper;

import com.franquicias.domain.model.Sucursal;
import com.franquicias.infrastructure.persistence.r2dbc.entity.SucursalEntity;

public class SucursalMapper {
    
    public static Sucursal toDomain(SucursalEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Sucursal(
            entity.getId(),
            entity.getNombre(),
            entity.getFranquiciaId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public static SucursalEntity toEntity(Sucursal domain) {
        if (domain == null) {
            return null;
        }
        return new SucursalEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getFranquiciaId(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }
}

