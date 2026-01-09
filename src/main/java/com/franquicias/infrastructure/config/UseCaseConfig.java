package com.franquicias.infrastructure.config;

import com.franquicias.application.usecase.*;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.ProductoRepository;
import com.franquicias.domain.repository.SucursalRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CrearFranquiciaUseCase crearFranquiciaUseCase(FranquiciaRepository franquiciaRepository) {
        return new CrearFranquiciaUseCase(franquiciaRepository);
    }

    @Bean
    public AgregarSucursalUseCase agregarSucursalUseCase(SucursalRepository sucursalRepository,
                                                         FranquiciaRepository franquiciaRepository) {
        return new AgregarSucursalUseCase(sucursalRepository, franquiciaRepository);
    }

    @Bean
    public AgregarProductoUseCase agregarProductoUseCase(ProductoRepository productoRepository,
                                                         SucursalRepository sucursalRepository) {
        return new AgregarProductoUseCase(productoRepository, sucursalRepository);
    }

    @Bean
    public EliminarProductoUseCase eliminarProductoUseCase(ProductoRepository productoRepository) {
        return new EliminarProductoUseCase(productoRepository);
    }

    @Bean
    public ModificarStockProductoUseCase modificarStockProductoUseCase(ProductoRepository productoRepository) {
        return new ModificarStockProductoUseCase(productoRepository);
    }

    @Bean
    public ObtenerProductosMaximoStockPorFranquiciaUseCase obtenerProductosMaximoStockPorFranquiciaUseCase(
            ProductoRepository productoRepository,
            FranquiciaRepository franquiciaRepository) {
        return new ObtenerProductosMaximoStockPorFranquiciaUseCase(productoRepository, franquiciaRepository);
    }

    @Bean
    public ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase(
            FranquiciaRepository franquiciaRepository) {
        return new ActualizarNombreFranquiciaUseCase(franquiciaRepository);
    }

    @Bean
    public ActualizarNombreSucursalUseCase actualizarNombreSucursalUseCase(
            SucursalRepository sucursalRepository) {
        return new ActualizarNombreSucursalUseCase(sucursalRepository);
    }

    @Bean
    public ActualizarNombreProductoUseCase actualizarNombreProductoUseCase(
            ProductoRepository productoRepository) {
        return new ActualizarNombreProductoUseCase(productoRepository);
    }
}

