package com.franquicias.adapters.presentation.controller;

import com.franquicias.adapters.presentation.dto.ProductoMaximoStockResponse;
import com.franquicias.adapters.presentation.mapper.ProductoMaximoStockMapper;
import com.franquicias.application.usecase.ObtenerProductosMaximoStockPorFranquiciaUseCase;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/productos-maximo-stock")
public class ProductoMaximoStockController {
    
    private final ObtenerProductosMaximoStockPorFranquiciaUseCase obtenerProductosMaximoStockPorFranquiciaUseCase;

    public ProductoMaximoStockController(ObtenerProductosMaximoStockPorFranquiciaUseCase obtenerProductosMaximoStockPorFranquiciaUseCase) {
        this.obtenerProductosMaximoStockPorFranquiciaUseCase = obtenerProductosMaximoStockPorFranquiciaUseCase;
    }

    @GetMapping
    public Flux<ProductoMaximoStockResponse> obtenerProductosMaximoStock(
            @PathVariable Long franquiciaId) {
        return obtenerProductosMaximoStockPorFranquiciaUseCase.execute(franquiciaId)
                .map(ProductoMaximoStockMapper::toResponse);
    }
}

