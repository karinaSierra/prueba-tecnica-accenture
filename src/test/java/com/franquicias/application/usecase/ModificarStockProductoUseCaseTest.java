package com.franquicias.application.usecase;

import com.franquicias.domain.model.Producto;
import com.franquicias.domain.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModificarStockProductoUseCaseTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ModificarStockProductoUseCase modificarStockProductoUseCase;

    @Test
    void execute_WhenProductoExistsAndStockIsValid_ShouldReturnProductoUpdated() {
        // Given
        Long productoId = 1L;
        Integer nuevoStock = 100;
        Producto productoExistente = new Producto(productoId, "Producto Test", 50, 1L);
        Producto productoActualizado = new Producto(productoId, "Producto Test", nuevoStock, 1L);
        
        when(productoRepository.findById(eq(productoId)))
                .thenReturn(Mono.just(productoExistente));
        when(productoRepository.update(any(Producto.class)))
                .thenReturn(Mono.just(productoActualizado));

        // When
        Mono<Producto> result = modificarStockProductoUseCase.execute(productoId, nuevoStock);

        // Then
        StepVerifier.create(result)
                .expectNextMatches(producto -> producto.getStock().equals(nuevoStock))
                .verifyComplete();
    }

    @Test
    void execute_WhenProductoDoesNotExist_ShouldReturnError() {
        // Given
        Long productoId = 999L;
        Integer nuevoStock = 100;
        
        when(productoRepository.findById(eq(productoId)))
                .thenReturn(Mono.empty());

        // When
        Mono<Producto> result = modificarStockProductoUseCase.execute(productoId, nuevoStock);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void execute_WhenStockIsNegative_ShouldReturnError() {
        // When
        Mono<Producto> result = modificarStockProductoUseCase.execute(1L, -1);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}

