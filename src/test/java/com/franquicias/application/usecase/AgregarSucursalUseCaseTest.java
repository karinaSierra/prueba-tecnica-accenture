package com.franquicias.application.usecase;

import com.franquicias.domain.model.Sucursal;
import com.franquicias.domain.repository.FranquiciaRepository;
import com.franquicias.domain.repository.SucursalRepository;
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
class AgregarSucursalUseCaseTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private AgregarSucursalUseCase agregarSucursalUseCase;

    @Test
    void execute_WhenFranquiciaExistsAndNombreIsValid_ShouldReturnSucursal() {
        // Given
        Long franquiciaId = 1L;
        String nombre = "Sucursal Test";
        Sucursal sucursalEsperada = new Sucursal(1L, nombre, franquiciaId);
        
        when(franquiciaRepository.existsById(eq(franquiciaId)))
                .thenReturn(Mono.just(true));
        when(sucursalRepository.save(any(Sucursal.class)))
                .thenReturn(Mono.just(sucursalEsperada));

        // When
        Mono<Sucursal> result = agregarSucursalUseCase.execute(franquiciaId, nombre);

        // Then
        StepVerifier.create(result)
                .expectNext(sucursalEsperada)
                .verifyComplete();
    }

    @Test
    void execute_WhenFranquiciaDoesNotExist_ShouldReturnError() {
        // Given
        Long franquiciaId = 999L;
        String nombre = "Sucursal Test";
        
        when(franquiciaRepository.existsById(eq(franquiciaId)))
                .thenReturn(Mono.just(false));

        // When
        Mono<Sucursal> result = agregarSucursalUseCase.execute(franquiciaId, nombre);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void execute_WhenNombreIsNull_ShouldReturnError() {
        // When
        Mono<Sucursal> result = agregarSucursalUseCase.execute(1L, null);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}

