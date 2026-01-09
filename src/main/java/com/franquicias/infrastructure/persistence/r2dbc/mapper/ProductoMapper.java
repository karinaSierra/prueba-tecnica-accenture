package com.franquicias.infrastructure.persistence.r2dbc.mapper;

import com.franquicias.domain.model.Producto;
import com.franquicias.infrastructure.persistence.r2dbc.entity.ProductoEntity;

public class ProductoMapper {
    
    public static Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Producto(
            entity.getId(),
            entity.getNombre(),
            entity.getStock(),
            entity.getSucursalId(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
    
    public static ProductoEntity toEntity(Producto domain) {
        if (domain == null) {
            return null;
        }
        return new ProductoEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getStock(),
            domain.getSucursalId(),
            domain.getCreatedAt(),
            domain.getUpdatedAt()
        );
    }
}

