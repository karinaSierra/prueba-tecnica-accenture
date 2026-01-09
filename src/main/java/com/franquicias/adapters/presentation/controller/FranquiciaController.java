package com.franquicias.adapters.presentation.controller;

import com.franquicias.adapters.presentation.dto.ActualizarNombreRequest;
import com.franquicias.adapters.presentation.dto.FranquiciaRequest;
import com.franquicias.adapters.presentation.dto.FranquiciaResponse;
import com.franquicias.adapters.presentation.mapper.FranquiciaMapper;
import com.franquicias.application.usecase.ActualizarNombreFranquiciaUseCase;
import com.franquicias.application.usecase.CrearFranquiciaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias")
public class FranquiciaController {
    
    private final CrearFranquiciaUseCase crearFranquiciaUseCase;
    private final ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase;

    public FranquiciaController(CrearFranquiciaUseCase crearFranquiciaUseCase,
                                ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase) {
        this.crearFranquiciaUseCase = crearFranquiciaUseCase;
        this.actualizarNombreFranquiciaUseCase = actualizarNombreFranquiciaUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<FranquiciaResponse>> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        return crearFranquiciaUseCase.execute(request.getNombre())
                .map(FranquiciaMapper::toResponse)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PutMapping("/{id}/nombre")
    public Mono<ResponseEntity<FranquiciaResponse>> actualizarNombreFranquicia(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreFranquiciaUseCase.execute(id, request.getNombre())
                .map(FranquiciaMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}

