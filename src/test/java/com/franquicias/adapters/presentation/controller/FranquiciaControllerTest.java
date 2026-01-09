package com.franquicias.adapters.presentation.controller;

import com.franquicias.adapters.presentation.dto.FranquiciaRequest;
import com.franquicias.application.usecase.ActualizarNombreFranquiciaUseCase;
import com.franquicias.application.usecase.CrearFranquiciaUseCase;
import com.franquicias.domain.model.Franquicia;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(FranquiciaController.class)
class FranquiciaControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private CrearFranquiciaUseCase crearFranquiciaUseCase;

    @MockBean
    private ActualizarNombreFranquiciaUseCase actualizarNombreFranquiciaUseCase;

    @Test
    void crearFranquicia_WhenRequestIsValid_ShouldReturnCreated() {
        // Given
        FranquiciaRequest request = new FranquiciaRequest("Franquicia Test");
        Franquicia franquicia = new Franquicia(1L, "Franquicia Test", 
                LocalDateTime.now(), LocalDateTime.now());
        
        when(crearFranquiciaUseCase.execute(any(String.class)))
                .thenReturn(Mono.just(franquicia));

        // When & Then
        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.nombre").isEqualTo("Franquicia Test");
    }

    @Test
    void crearFranquicia_WhenNombreIsEmpty_ShouldReturnBadRequest() {
        // Given
        FranquiciaRequest request = new FranquiciaRequest("");

        // When & Then
        webTestClient.post()
                .uri("/api/franquicias")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void actualizarNombreFranquicia_WhenRequestIsValid_ShouldReturnOk() {
        // Given
        Long franquiciaId = 1L;
        Franquicia franquicia = new Franquicia(franquiciaId, "Nuevo Nombre", 
                LocalDateTime.now(), LocalDateTime.now());
        
        when(actualizarNombreFranquiciaUseCase.execute(eq(franquiciaId), any(String.class)))
                .thenReturn(Mono.just(franquicia));

        // When & Then
        webTestClient.put()
                .uri("/api/franquicias/{id}/nombre", franquiciaId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"nombre\": \"Nuevo Nombre\"}")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nombre").isEqualTo("Nuevo Nombre");
    }
}

