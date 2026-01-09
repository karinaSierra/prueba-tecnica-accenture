# 📚 Buenas Prácticas - API Reactiva de Franquicias

Este documento contiene recomendaciones específicas para el desarrollo y mantenimiento de esta API reactiva, basadas en las mejores prácticas de programación reactiva, Clean Architecture y Spring WebFlux.

## 🎯 Principios Fundamentales

### 1. Programación Reactiva

#### ✅ Hacer
- **Usar Mono y Flux exclusivamente**: Todo el flujo debe ser reactivo desde el controlador hasta el repositorio
- **Encadenar operadores reactivos**: Usar `map`, `flatMap`, `filter`, `switchIfEmpty`, `onErrorResume`
- **Manejar errores reactivamente**: Usar `onErrorReturn`, `onErrorResume`, `onErrorMap`

```java
// ✅ Correcto
return repository.findById(id)
    .switchIfEmpty(Mono.error(new EntityNotFoundException()))
    .map(this::toResponse);

// ❌ Incorrecto
return Mono.just(repository.findById(id).block()); // NUNCA usar block()
```

#### ❌ No Hacer
- **NUNCA usar `block()`**: Bloquea el hilo y rompe la reactividad
- **NUNCA usar `subscribe()` en el código de negocio**: Solo en tests o puntos de entrada
- **NUNCA mezclar código bloqueante con reactivo**: Usar adaptadores si es necesario

### 2. Clean Architecture

#### Estructura de Capas

```
domain/          → Entidades puras, interfaces de repositorio
application/     → Casos de uso, lógica de negocio
infrastructure/   → Implementaciones técnicas (R2DBC, config)
adapters/         → Controladores, DTOs, mappers
common/           → Utilidades compartidas
```

#### Reglas de Dependencias

- ✅ **Domain** no depende de nada
- ✅ **Application** depende solo de **Domain**
- ✅ **Infrastructure** depende de **Domain** y **Application**
- ✅ **Adapters** depende de **Domain** y **Application**
- ❌ **Domain** NUNCA depende de frameworks externos

### 3. Casos de Uso (Use Cases)

#### Características
- **Una responsabilidad**: Cada caso de uso hace una cosa
- **Validación de entrada**: Validar parámetros antes de procesar
- **Manejo de errores**: Retornar `Mono.error()` con excepciones descriptivas
- **Sin dependencias de frameworks**: Solo dependen de interfaces del dominio

```java
// ✅ Ejemplo de caso de uso bien estructurado
public Mono<Producto> execute(Long productoId, Integer nuevoStock) {
    // 1. Validación
    if (productoId == null) {
        return Mono.error(new IllegalArgumentException("..."));
    }
    
    // 2. Lógica de negocio reactiva
    return repository.findById(productoId)
        .switchIfEmpty(Mono.error(new EntityNotFoundException()))
        .flatMap(producto -> {
            producto.setStock(nuevoStock);
            return repository.update(producto);
        });
}
```

### 4. Repositorios R2DBC

#### Patrón de Implementación

```java
// 1. Interfaz Spring Data R2DBC (para operaciones simples)
interface EntitySpringDataRepository extends R2dbcRepository<Entity, Long> {
    Flux<Entity> findByField(Long field);
}

// 2. Implementación del repositorio de dominio
@Repository
public class EntityR2dbcRepository implements EntityRepository {
    private final EntitySpringDataRepository springDataRepository;
    private final DatabaseClient databaseClient; // Para queries complejas
    
    // Implementación reactiva
}
```

#### Queries Complejas
- Usar `DatabaseClient` para queries SQL personalizadas
- Mantener queries reactivas (retornar `Mono` o `Flux`)
- Usar parámetros nombrados para prevenir SQL injection

### 5. Controladores WebFlux

#### Estructura Recomendada

```java
@RestController
@RequestMapping("/api/resource")
public class ResourceController {
    
    private final UseCase useCase;
    
    @PostMapping
    public Mono<ResponseEntity<ResponseDTO>> create(@Valid @RequestBody RequestDTO request) {
        return useCase.execute(request.getField())
            .map(this::toResponse)
            .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response))
            .onErrorResume(this::handleError);
    }
}
```

#### Validación
- Usar `@Valid` en DTOs de entrada
- Validaciones con Bean Validation (`@NotBlank`, `@NotNull`, `@Min`, etc.)
- El `GlobalExceptionHandler` captura errores de validación

### 6. Testing

#### Pruebas Unitarias de Casos de Uso

```java
@ExtendWith(MockitoExtension.class)
class UseCaseTest {
    @Mock
    private Repository repository;
    
    @InjectMocks
    private UseCase useCase;
    
    @Test
    void execute_WhenValid_ShouldReturnResult() {
        // Given
        when(repository.findById(any())).thenReturn(Mono.just(entity));
        
        // When
        Mono<Result> result = useCase.execute(id);
        
        // Then
        StepVerifier.create(result)
            .expectNextMatches(r -> r.getId().equals(id))
            .verifyComplete();
    }
}
```

#### Pruebas de Controladores

```java
@WebFluxTest(Controller.class)
class ControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    
    @MockBean
    private UseCase useCase;
    
    @Test
    void create_WhenValid_ShouldReturnCreated() {
        when(useCase.execute(any())).thenReturn(Mono.just(result));
        
        webTestClient.post()
            .uri("/api/resource")
            .bodyValue(request)
            .exchange()
            .expectStatus().isCreated();
    }
}
```

### 7. Manejo de Errores

#### Estrategia Global

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleIllegalArgument(
            IllegalArgumentException ex, ServerWebExchange exchange) {
        // Retornar Mono con respuesta estructurada
        return Mono.just(ResponseEntity.badRequest()
            .body(new ErrorResponse(...)));
    }
}
```

#### Tipos de Errores
- **400 Bad Request**: Validación fallida, parámetros inválidos
- **404 Not Found**: Recurso no encontrado
- **500 Internal Server Error**: Errores inesperados

### 8. Configuración

#### application.yml
- Separar configuraciones por perfil (dev, test, prod)
- Usar variables de entorno para datos sensibles
- Configurar pool de conexiones R2DBC apropiadamente

#### Beans de Configuración
- Crear `@Configuration` para casos de uso
- Inyectar dependencias explícitamente
- Evitar `@ComponentScan` innecesario

### 9. Performance y Escalabilidad

#### Optimizaciones R2DBC
- Configurar pool de conexiones según carga esperada
- Usar índices en base de datos para queries frecuentes
- Evitar N+1 queries usando `flatMap` apropiadamente

#### Backpressure
- WebFlux maneja backpressure automáticamente
- Usar `limitRate()` si es necesario controlar el flujo
- Monitorear métricas de Reactor

### 10. Código Limpio

#### Nombres Descriptivos
```java
// ✅ Bueno
Mono<Producto> obtenerProductoConMayorStockPorFranquicia(Long franquiciaId)

// ❌ Malo
Mono<Producto> get(Long id)
```

#### Métodos Pequeños
- Un método debe hacer una cosa
- Máximo 20-30 líneas por método
- Extraer lógica compleja a métodos privados

#### Comentarios
- Comentar el "por qué", no el "qué"
- Usar nombres descriptivos en lugar de comentarios
- Documentar casos de uso complejos

## 🔍 Checklist de Revisión de Código

Antes de hacer commit, verificar:

- [ ] No hay uso de `block()`, `subscribe()` en código de producción
- [ ] Todos los métodos retornan `Mono` o `Flux`
- [ ] Validación de entrada en casos de uso
- [ ] Manejo de errores apropiado
- [ ] Tests unitarios con `StepVerifier`
- [ ] Cobertura de código ≥ 80%
- [ ] DTOs con validación Bean Validation
- [ ] Mappers para conversión entre capas
- [ ] Sin dependencias circulares
- [ ] Código sigue principios SOLID

## 📊 Métricas de Calidad

### Cobertura de Código
- **Mínimo**: 80% (configurado en JaCoCo)
- **Ideal**: 85-90%
- **Foco**: Casos de uso y lógica de negocio

### Complejidad Ciclomática
- **Máximo recomendado**: 10 por método
- Usar herramientas como SonarQube para monitoreo

### Deuda Técnica
- Revisar regularmente con SonarQube
- Priorizar refactorización de código complejo

## 🚀 Mejoras Futuras

### Posibles Extensiones
1. **Caché Reactivo**: Implementar caché con Redis y Spring Data Reactive
2. **Eventos de Dominio**: Implementar Domain Events para desacoplamiento
3. **Saga Pattern**: Para transacciones distribuidas
4. **Circuit Breaker**: Con Resilience4j para resiliencia
5. **Métricas**: Integrar Micrometer y Prometheus
6. **Documentación API**: Swagger/OpenAPI con SpringDoc
7. **Autenticación**: Spring Security Reactive con JWT
8. **Rate Limiting**: Control de tasa de requests

## 📚 Recursos Adicionales

- [Project Reactor Documentation](https://projectreactor.io/docs/core/release/reference/)
- [Spring WebFlux Reference](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [R2DBC Documentation](https://r2dbc.io/)
- [Clean Architecture by Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**Nota**: Estas prácticas están diseñadas para mantener el código mantenible, escalable y alineado con los principios de programación reactiva y Clean Architecture.

