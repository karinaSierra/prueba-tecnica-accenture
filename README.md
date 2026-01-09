# 🏢 API REST Reactiva - Gestión de Franquicias

API REST reactiva desarrollada con **Java 21**, **Spring Boot 3**, **Spring WebFlux** y **R2DBC** para la gestión de franquicias, sucursales y productos. Implementada siguiendo principios de **Clean Architecture**, programación funcional reactiva y estándares profesionales de desarrollo backend.

## 📋 Tabla de Contenidos

- [Características](#características)
- [Stack Tecnológico](#stack-tecnológico)
- [Arquitectura](#arquitectura)
- [Modelo de Dominio](#modelo-de-dominio)
- [Endpoints](#endpoints)
- [Requisitos Previos](#requisitos-previos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Pruebas](#pruebas)
- [Infraestructura](#infraestructura)
- [Buenas Prácticas](#buenas-prácticas)

## ✨ Características

- ✅ **Programación Reactiva**: Uso exclusivo de `Mono` y `Flux` sin código bloqueante
- ✅ **Clean Architecture**: Separación clara de responsabilidades en capas
- ✅ **R2DBC**: Acceso a datos reactivo con MySQL (sin JPA/Hibernate)
- ✅ **Pruebas Unitarias**: Cobertura mínima del 80% con JaCoCo
- ✅ **Contenerización**: Docker y Docker Compose
- ✅ **Infrastructure as Code**: Terraform para gestión de infraestructura
- ✅ **Manejo Global de Errores**: Respuestas consistentes y estructuradas
- ✅ **Validación de Datos**: Bean Validation en DTOs

## 🛠 Stack Tecnológico

### Backend
- **Java 21**: Lenguaje de programación
- **Spring Boot 3.2.0**: Framework principal
- **Spring WebFlux**: Programación reactiva
- **R2DBC MySQL**: Driver reactivo para MySQL
- **Spring Data R2DBC**: Abstracción de acceso a datos reactivo

### Base de Datos
- **MySQL 8.0**: Base de datos relacional

### Testing
- **JUnit 5**: Framework de pruebas
- **Mockito**: Mocking framework
- **StepVerifier**: Testing de streams reactivos
- **JaCoCo**: Análisis de cobertura de código (≥80%)

### Infraestructura
- **Docker**: Contenerización
- **Docker Compose**: Orquestación de contenedores
- **Terraform**: Infrastructure as Code

## 🏗 Arquitectura

El proyecto sigue los principios de **Clean Architecture** con las siguientes capas:

```
src/main/java/com/franquicias/
├── domain/                    # Capa de Dominio
│   ├── model/                 # Entidades de negocio
│   └── repository/            # Interfaces de repositorio
├── application/               # Capa de Aplicación
│   └── usecase/              # Casos de uso (lógica de negocio)
├── infrastructure/            # Capa de Infraestructura
│   ├── persistence/          # Implementación de repositorios (R2DBC)
│   └── config/               # Configuraciones
├── adapters/                  # Capa de Adaptadores
│   └── presentation/          # Controladores WebFlux, DTOs, Mappers
└── common/                    # Utilidades compartidas
    └── exception/            # Manejo global de errores
```

### Principios Aplicados

1. **Dependency Inversion**: Las capas internas no dependen de las externas
2. **Separation of Concerns**: Cada capa tiene una responsabilidad específica
3. **Reactive Programming**: Todo el flujo es no bloqueante
4. **Testability**: Cada capa es testeable de forma independiente

## 📊 Modelo de Dominio

### Entidades

- **Franquicia**: Representa una franquicia
  - `id`: Identificador único
  - `nombre`: Nombre de la franquicia

- **Sucursal**: Representa una sucursal de una franquicia
  - `id`: Identificador único
  - `nombre`: Nombre de la sucursal
  - `franquiciaId`: Referencia a la franquicia

- **Producto**: Representa un producto de una sucursal
  - `id`: Identificador único
  - `nombre`: Nombre del producto
  - `stock`: Cantidad en stock
  - `sucursalId`: Referencia a la sucursal

### Relaciones

- Una **Franquicia** tiene muchas **Sucursales** (1:N)
- Una **Sucursal** pertenece a una **Franquicia** (N:1)
- Una **Sucursal** tiene muchos **Productos** (1:N)
- Un **Producto** pertenece a una **Sucursal** (N:1)

## 🔌 Endpoints

### Endpoints Obligatorios

#### 1. Crear Franquicia
```http
POST /api/franquicias
Content-Type: application/json

{
  "nombre": "Franquicia Ejemplo"
}
```

#### 2. Agregar Sucursal a una Franquicia
```http
POST /api/franquicias/{franquiciaId}/sucursales
Content-Type: application/json

{
  "nombre": "Sucursal Centro"
}
```

#### 3. Agregar Producto a una Sucursal
```http
POST /api/sucursales/{sucursalId}/productos
Content-Type: application/json

{
  "nombre": "Producto Ejemplo",
  "stock": 100
}
```

#### 4. Eliminar Producto de una Sucursal
```http
DELETE /api/sucursales/{sucursalId}/productos/{productoId}
```

#### 5. Modificar Stock de un Producto
```http
PUT /api/sucursales/{sucursalId}/productos/{productoId}/stock
Content-Type: application/json

{
  "stock": 150
}
```

#### 6. Obtener Productos con Mayor Stock por Franquicia
```http
GET /api/franquicias/{franquiciaId}/productos-maximo-stock
```

Retorna el producto con mayor stock de cada sucursal de la franquicia, indicando a qué sucursal pertenece.

### Endpoints PLUS

#### 7. Actualizar Nombre de Franquicia
```http
PUT /api/franquicias/{id}/nombre
Content-Type: application/json

{
  "nombre": "Nuevo Nombre"
}
```

#### 8. Actualizar Nombre de Sucursal
```http
PUT /api/franquicias/{franquiciaId}/sucursales/{sucursalId}/nombre
Content-Type: application/json

{
  "nombre": "Nuevo Nombre"
}
```

#### 9. Actualizar Nombre de Producto
```http
PUT /api/sucursales/{sucursalId}/productos/{productoId}/nombre
Content-Type: application/json

{
  "nombre": "Nuevo Nombre"
}
```

## 📦 Requisitos Previos

- **Java 21** o superior
- **Maven 3.9+**
- **Docker** y **Docker Compose** (para ejecución con contenedores)
- **Terraform** (opcional, para IaC)

## 🚀 Instalación y Ejecución

> **⚠️ Importante**: Si es la primera vez que usas Docker Desktop, consulta la [Guía de Docker Desktop](GUIA_DOCKER_DESKTOP.md) para configurarlo correctamente.

### Opción 1: Docker Compose (Recomendado)

1. **Asegúrate de que Docker Desktop esté ejecutándose**:
   - Verifica el ícono de Docker en la bandeja del sistema
   - Debe estar estable (sin animación)
   - Si no está ejecutándose, inícialo desde el menú de Windows

2. **Verificar que Docker funciona**:
```powershell
docker ps
```

3. **Navegar al directorio del proyecto**:
```bash
cd franquicias-api
```

4. **Ejecutar con Docker Compose**:
```bash
docker-compose up -d
```

5. **Verificar que los contenedores están ejecutándose**:
```bash
docker ps
```

Deberías ver dos contenedores: `franquicias-mysql` y `franquicias-api`

La aplicación estará disponible en `http://localhost:8080`

**Ver logs si es necesario**:
```bash
docker-compose logs -f app
```

### Opción 2: Ejecución Local

1. Asegúrate de tener MySQL ejecutándose:
```bash
# Con Docker
docker run -d \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -e MYSQL_DATABASE=franquicias_db \
  -e MYSQL_USER=franquicias_user \
  -e MYSQL_PASSWORD=franquicias_pass \
  -p 3306:3306 \
  mysql:8.0
```

2. Ejecutar el script de inicialización de la base de datos:
```bash
mysql -u franquicias_user -pfranquicias_pass franquicias_db < src/main/resources/schema.sql
```

3. Compilar y ejecutar la aplicación:
```bash
mvn clean install
mvn spring-boot:run
```

### Opción 3: Terraform (Infrastructure as Code)

1. Navegar al directorio de Terraform:
```bash
cd terraform
```

2. Inicializar Terraform:
```bash
terraform init
```

3. Planificar la infraestructura:
```bash
terraform plan
```

4. Aplicar la configuración:
```bash
terraform apply
```

## 🧪 Pruebas

### Ejecutar Pruebas Unitarias

```bash
mvn test
```

### Verificar Cobertura de Código

```bash
mvn clean test jacoco:report
```

El reporte de cobertura estará disponible en:
```
target/site/jacoco/index.html
```

### Verificar Threshold de Cobertura

```bash
mvn clean verify
```

Este comando fallará si la cobertura es menor al 80% configurado.

### Ejecutar Pruebas con Docker

```bash
docker-compose -f docker-compose.test.yml up --abort-on-container-exit
```

## 🏗 Infraestructura

### Docker

- **Dockerfile**: Multi-stage build optimizado para producción
- **docker-compose.yml**: Orquestación de servicios (app + MySQL)

### Terraform

- **main.tf**: Configuración de infraestructura
- **terraform.tfvars.example**: Ejemplo de variables

### Variables de Entorno

```yaml
SPRING_R2DBC_URL: r2dbc:mysql://localhost:3306/franquicias_db?useSSL=false&allowPublicKeyRetrieval=true
SPRING_R2DBC_USERNAME: franquicias_user
SPRING_R2DBC_PASSWORD: franquicias_pass
```

## 💡 Buenas Prácticas Implementadas

### Programación Reactiva

- ✅ Uso exclusivo de `Mono` y `Flux`
- ✅ No uso de `block()`, `subscribe()` ni APIs sincrónicas
- ✅ Operadores reactivos (`map`, `flatMap`, `filter`, `switchIfEmpty`)
- ✅ Manejo de errores con `onErrorResume`, `onErrorReturn`

### Clean Architecture

- ✅ Separación clara de capas
- ✅ Dependencias hacia adentro (Dependency Rule)
- ✅ Interfaces en la capa de dominio
- ✅ Implementaciones en infraestructura

### Testing

- ✅ Pruebas unitarias con `StepVerifier` para streams reactivos
- ✅ Mocks con Mockito para aislar dependencias
- ✅ Cobertura mínima del 80% con JaCoCo
- ✅ Pruebas de integración con Testcontainers (opcional)

### Código Limpio

- ✅ Nombres descriptivos y significativos
- ✅ Métodos pequeños y con responsabilidad única
- ✅ Validación de entrada en casos de uso
- ✅ Manejo global de excepciones

### Seguridad y Validación

- ✅ Validación de DTOs con Bean Validation
- ✅ Manejo de errores estructurado
- ✅ Respuestas HTTP apropiadas

## 📝 Ejemplos de Uso

### Crear una Franquicia y su Primera Sucursal

```bash
# 1. Crear franquicia
curl -X POST http://localhost:8080/api/franquicias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "McDonalds"}'

# Respuesta: {"id": 1, "nombre": "McDonalds", ...}

# 2. Agregar sucursal
curl -X POST http://localhost:8080/api/franquicias/1/sucursales \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Sucursal Centro"}'

# 3. Agregar producto
curl -X POST http://localhost:8080/api/sucursales/1/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Big Mac", "stock": 50}'
```

### Obtener Productos con Mayor Stock

```bash
curl http://localhost:8080/api/franquicias/1/productos-maximo-stock
```

## 🔍 Troubleshooting

### Problemas Comunes

1. **Error de conexión a MySQL**:
   - Verificar que MySQL esté ejecutándose
   - Revisar credenciales en `application.yml`
   - Verificar que el puerto 3306 esté disponible

2. **Error de compilación**:
   - Verificar versión de Java (debe ser 21)
   - Ejecutar `mvn clean install`

3. **Cobertura de código insuficiente**:
   - Ejecutar `mvn jacoco:report` para ver detalles
   - Agregar más pruebas unitarias

## 📄 Licencia

Este proyecto es una prueba técnica de desarrollo backend.

## 👤 Autor

Desarrollado como prueba técnica para evaluación de habilidades en:
- Programación Reactiva
- Clean Architecture
- Spring Boot 3 y WebFlux
- Pruebas Automatizadas
- Contenerización e IaC

---

**Nota**: Este proyecto está diseñado como una demostración de habilidades técnicas en desarrollo backend profesional con enfoque en programación reactiva y arquitectura limpia.

#   p r u e b a - t e c n i c a - a c c e n t u r e 
 
 