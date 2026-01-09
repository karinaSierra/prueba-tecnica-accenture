package com.franquicias.application.usecase;

import com.franquicias.domain.model.Franquicia;
import com.franquicias.domain.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearFranquiciaUseCaseTest {

    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private CrearFranquiciaUseCase crearFranquiciaUseCase;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_WhenNombreIsValid_ShouldReturnFranquicia() {
        // Given
        String nombre = "Franquicia Test";
        Franquicia franquiciaEsperada = new Franquicia(1L, nombre);
        
        when(franquiciaRepository.save(any(Franquicia.class)))
                .thenReturn(Mono.just(franquiciaEsperada));

        // When
        Mono<Franquicia> result = crearFranquiciaUseCase.execute(nombre);

        // Then
        StepVerifier.create(result)
                .expectNext(franquiciaEsperada)
                .verifyComplete();
    }

    @Test
    void execute_WhenNombreIsNull_ShouldReturnError() {
        // When
        Mono<Franquicia> result = crearFranquiciaUseCase.execute(null);

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void execute_WhenNombreIsEmpty_ShouldReturnError() {
        // When
        Mono<Franquicia> result = crearFranquiciaUseCase.execute("   ");

        // Then
        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}

