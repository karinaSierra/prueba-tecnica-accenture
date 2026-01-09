package com.franquicias.adapters.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public class SucursalRequest {
    
    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;

    public SucursalRequest() {
    }

    public SucursalRequest(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}

