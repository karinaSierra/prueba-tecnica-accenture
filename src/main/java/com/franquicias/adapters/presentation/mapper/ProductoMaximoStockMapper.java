package com.franquicias.adapters.presentation.mapper;

import com.franquicias.adapters.presentation.dto.ProductoMaximoStockResponse;
import com.franquicias.domain.model.ProductoMaximoStock;

public class ProductoMaximoStockMapper {
    
    public static ProductoMaximoStockResponse toResponse(ProductoMaximoStock productoMaximoStock) {
        if (productoMaximoStock == null) {
            return null;
        }
        return new ProductoMaximoStockResponse(
            productoMaximoStock.getProductoId(),
            productoMaximoStock.getProductoNombre(),
            productoMaximoStock.getStock(),
            productoMaximoStock.getSucursalId(),
            productoMaximoStock.getSucursalNombre()
        );
    }
}

