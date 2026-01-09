package com.franquicias.domain.model;

import java.time.LocalDateTime;

public class Sucursal {
    private Long id;
    private String nombre;
    private Long franquiciaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Sucursal() {
    }

    public Sucursal(Long id, String nombre, Long franquiciaId) {
        this.id = id;
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
    }

    public Sucursal(Long id, String nombre, Long franquiciaId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nombre = nombre;
        this.franquiciaId = franquiciaId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getFranquiciaId() {
        return franquiciaId;
    }

    public void setFranquiciaId(Long franquiciaId) {
        this.franquiciaId = franquiciaId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

