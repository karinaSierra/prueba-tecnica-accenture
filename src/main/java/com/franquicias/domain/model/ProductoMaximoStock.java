package com.franquicias.domain.model;

public class ProductoMaximoStock {
    private Long productoId;
    private String productoNombre;
    private Integer stock;
    private Long sucursalId;
    private String sucursalNombre;

    public ProductoMaximoStock() {
    }

    public ProductoMaximoStock(Long productoId, String productoNombre, Integer stock, 
                               Long sucursalId, String sucursalNombre) {
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.stock = stock;
        this.sucursalId = sucursalId;
        this.sucursalNombre = sucursalNombre;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productoNombre) {
        this.productoNombre = productoNombre;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Long sucursalId) {
        this.sucursalId = sucursalId;
    }

    public String getSucursalNombre() {
        return sucursalNombre;
    }

    public void setSucursalNombre(String sucursalNombre) {
        this.sucursalNombre = sucursalNombre;
    }
}

