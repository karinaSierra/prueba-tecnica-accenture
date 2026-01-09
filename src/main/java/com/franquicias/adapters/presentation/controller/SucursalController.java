package com.franquicias.adapters.presentation.controller;

import com.franquicias.adapters.presentation.dto.ActualizarNombreRequest;
import com.franquicias.adapters.presentation.dto.SucursalRequest;
import com.franquicias.adapters.presentation.dto.SucursalResponse;
import com.franquicias.application.usecase.ActualizarNombreSucursalUseCase;
import com.franquicias.application.usecase.AgregarSucursalUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/franquicias/{franquiciaId}/sucursales")
public class SucursalController {
    
    private final AgregarSucursalUseCase agregarSucursalUseCase;
    private final ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase;

    public SucursalController(AgregarSucursalUseCase agregarSucursalUseCase,
                              ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase) {
        this.agregarSucursalUseCase = agregarSucursalUseCase;
        this.actualizarNombreSucursalUseCase = actualizarNombreSucursalUseCase;
    }

    @PostMapping
    public Mono<ResponseEntity<SucursalResponse>> agregarSucursal(
            @PathVariable Long franquiciaId,
            @Valid @RequestBody SucursalRequest request) {
        return agregarSucursalUseCase.execute(franquiciaId, request.getNombre())
                .map(sucursal -> new SucursalResponse(
                    sucursal.getId(),
                    sucursal.getNombre(),
                    sucursal.getFranquiciaId(),
                    sucursal.getCreatedAt(),
                    sucursal.getUpdatedAt()
                ))
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PutMapping("/{sucursalId}/nombre")
    public Mono<ResponseEntity<SucursalResponse>> actualizarNombreSucursal(
            @PathVariable Long sucursalId,
            @Valid @RequestBody ActualizarNombreRequest request) {
        return actualizarNombreSucursalUseCase.execute(sucursalId, request.getNombre())
                .map(sucursal -> new SucursalResponse(
                    sucursal.getId(),
                    sucursal.getNombre(),
                    sucursal.getFranquiciaId(),
                    sucursal.getCreatedAt(),
                    sucursal.getUpdatedAt()
                ))
                .map(ResponseEntity::ok);
    }
}

