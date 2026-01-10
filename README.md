# 🏢 API REST Reactiva – Gestión de Franquicias

## Título del Proyecto

**Franquicias API** - API REST Reactiva para Gestión de Franquicias, Sucursales y Productos

---

## Descripción General del Proyecto

Este proyecto es una **API REST reactiva** desarrollada con **Java 21** y **Spring Boot 3** que permite gestionar un sistema jerárquico de franquicias, sus sucursales asociadas y los productos que se almacenan en cada sucursal. La aplicación implementa arquitectura limpia (Clean Architecture), programación reactiva con Spring WebFlux, acceso a datos reactivo con R2DBC, y está completamente contenedorizada con Docker.

La API proporciona endpoints para crear franquicias, agregar sucursales a franquicias existentes, gestionar productos en cada sucursal (crear, actualizar stock, eliminar) y consultar información agregada como los productos con mayor stock por franquicia.

---

## Objetivo de la Prueba Técnica

Este proyecto fue desarrollado como **prueba técnica** para demostrar las siguientes capacidades técnicas:

- **Programación Reactiva**: Uso exclusivo de programación reactiva con `Mono` y `Flux` de Reactor, sin bloqueos de hilos
- **Clean Architecture**: Implementación de arquitectura limpia con separación clara de capas (Domain, Application, Infrastructure, Adapters)
- **Acceso a Datos Reactivo**: Utilización de R2DBC para acceso reactivo a MySQL, evitando el bloqueo de hilos característico de JPA/Hibernate
- **Pruebas Unitarias**: Cobertura de código con pruebas unitarias utilizando JUnit 5, Mockito y StepVerifier
- **Contenedorización**: Dockerización completa de la aplicación y su base de datos con Docker Compose
- **Infrastructure as Code**: Despliegue automatizado en AWS utilizando AWS CDK (TypeScript)
- **Calidad de Código**: Implementación de mejores prácticas, validación de datos, manejo global de errores y configuración de JaCoCo para verificación de cobertura de código

---

## Tecnologías Utilizadas

### Backend
- **Java 21** - Lenguaje de programación (LTS)
- **Spring Boot 3.2.0** - Framework principal
- **Spring WebFlux** - Programación reactiva y no bloqueante
- **Spring Data R2DBC** - Abstracción de acceso a datos reactivo
- **R2DBC MySQL Driver 1.0.5** - Driver reactivo para MySQL
- **Lombok** - Reducción de boilerplate
- **Spring Boot Validation** - Validación de DTOs con Bean Validation
- **Spring Boot Actuator** - Health checks y monitoreo

### Base de Datos
- **MySQL 8.0** - Base de datos relacional

### Testing
- **JUnit 5** - Framework de pruebas unitarias
- **Mockito** - Framework de mocking
- **Reactor Test (StepVerifier)** - Testing de streams reactivos
- **Testcontainers 1.19.3** - Contenedores para pruebas de integración
- **JaCoCo 0.8.11** - Análisis y reporte de cobertura de código

### Infraestructura y DevOps
- **Docker** - Contenedorización de la aplicación
- **Docker Compose** - Orquestación de contenedores locales
- **Maven 3.9+** - Gestión de dependencias y construcción
- **AWS CDK (TypeScript)** - Infrastructure as Code para AWS
  - EC2 con Amazon Linux 2023
  - VPC y Security Groups
  - CloudWatch Logs
  - IAM Roles

---

## Arquitectura del Proyecto

El proyecto sigue los principios de **Clean Architecture** (Arquitectura Limpia) de Robert C. Martin, con las siguientes características:

### Principios Aplicados

1. **Dependency Inversion Principle**: Las capas internas (domain, application) no dependen de las capas externas (infrastructure, adapters)
2. **Separation of Concerns**: Cada capa tiene una responsabilidad específica y única
3. **Single Responsibility Principle**: Cada clase y módulo tiene una única razón para cambiar
4. **Reactive Programming**: Todo el flujo de datos es no bloqueante, desde el controlador hasta la base de datos

### Capas de la Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                  Adapters (Presentación)                │
│  - Controllers (WebFlux)                                │
│  - DTOs (Request/Response)                              │
│  - Mappers (Domain ↔ DTO)                               │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│              Application (Casos de Uso)                  │
│  - Use Cases (Lógica de negocio)                        │
│  - Independiente de framework y tecnologías             │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                  Domain (Dominio)                        │
│  - Modelos de dominio (Entidades)                       │
│  - Interfaces de repositorios                           │
│  - Reglas de negocio puras                              │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│           Infrastructure (Infraestructura)               │
│  - Implementación de repositorios (R2DBC)               │
│  - Configuración de frameworks                          │
│  - Entidades de persistencia                            │
└─────────────────────────────────────────────────────────┘
```

### Flujo de Datos

1. **Request** → Controller (Adapter) recibe petición HTTP
2. **Mapping** → DTO se convierte a modelo de dominio
3. **Use Case** → Se ejecuta la lógica de negocio (Application)
4. **Repository** → Se accede a los datos a través de la interfaz (Domain)
5. **Implementation** → La implementación R2DBC ejecuta la consulta (Infrastructure)
6. **Response** → El resultado se convierte a DTO y se retorna

---

## Estructura de Carpetas

```
franquicias-api/
├── src/
│   ├── main/
│   │   ├── java/com/franquicias/
│   │   │   ├── domain/                          # Capa de Dominio
│   │   │   │   ├── model/                       # Entidades de negocio
│   │   │   │   │   ├── Franquicia.java
│   │   │   │   │   ├── Sucursal.java
│   │   │   │   │   ├── Producto.java
│   │   │   │   │   └── ProductoMaximoStock.java
│   │   │   │   └── repository/                  # Interfaces de repositorio
│   │   │   │       ├── FranquiciaRepository.java
│   │   │   │       ├── SucursalRepository.java
│   │   │   │       └── ProductoRepository.java
│   │   │   │
│   │   │   ├── application/                     # Capa de Aplicación
│   │   │   │   └── usecase/                     # Casos de uso
│   │   │   │       ├── CrearFranquiciaUseCase.java
│   │   │   │       ├── AgregarSucursalUseCase.java
│   │   │   │       ├── AgregarProductoUseCase.java
│   │   │   │       ├── EliminarProductoUseCase.java
│   │   │   │       ├── ModificarStockProductoUseCase.java
│   │   │   │       ├── ObtenerProductosMaximoStockPorFranquiciaUseCase.java
│   │   │   │       ├── ActualizarNombreFranquiciaUseCase.java
│   │   │   │       ├── ActualizarNombreSucursalUseCase.java
│   │   │   │       └── ActualizarNombreProductoUseCase.java
│   │   │   │
│   │   │   ├── infrastructure/                  # Capa de Infraestructura
│   │   │   │   ├── config/                      # Configuraciones
│   │   │   │   │   ├── R2dbcConfig.java
│   │   │   │   │   └── UseCaseConfig.java
│   │   │   │   └── persistence/                 # Persistencia
│   │   │   │       └── r2dbc/
│   │   │   │           ├── entity/              # Entidades de persistencia
│   │   │   │           │   ├── FranquiciaEntity.java
│   │   │   │           │   ├── SucursalEntity.java
│   │   │   │           │   └── ProductoEntity.java
│   │   │   │           ├── mapper/              # Mappers Entity ↔ Domain
│   │   │   │           │   ├── FranquiciaMapper.java
│   │   │   │           │   ├── SucursalMapper.java
│   │   │   │           │   └── ProductoMapper.java
│   │   │   │           └── repository/          # Implementación repositorios
│   │   │   │               ├── FranquiciaR2dbcRepository.java
│   │   │   │               ├── SucursalR2dbcRepository.java
│   │   │   │               └── ProductoR2dbcRepository.java
│   │   │   │
│   │   │   ├── adapters/                        # Capa de Adaptadores
│   │   │   │   └── presentation/                # Presentación (API REST)
│   │   │   │       ├── controller/              # Controladores WebFlux
│   │   │   │       │   ├── FranquiciaController.java
│   │   │   │       │   ├── SucursalController.java
│   │   │   │       │   ├── ProductoController.java
│   │   │   │       │   └── ProductoMaximoStockController.java
│   │   │   │       ├── dto/                     # Data Transfer Objects
│   │   │   │       │   ├── FranquiciaRequest.java
│   │   │   │       │   ├── FranquiciaResponse.java
│   │   │   │       │   ├── SucursalRequest.java
│   │   │   │       │   ├── SucursalResponse.java
│   │   │   │       │   ├── ProductoRequest.java
│   │   │   │       │   ├── ProductoResponse.java
│   │   │   │       │   ├── ModificarStockRequest.java
│   │   │   │       │   ├── ActualizarNombreRequest.java
│   │   │   │       │   └── ProductoMaximoStockResponse.java
│   │   │   │       └── mapper/                  # Mappers Domain ↔ DTO
│   │   │   │           ├── FranquiciaMapper.java
│   │   │   │           └── ProductoMaximoStockMapper.java
│   │   │   │
│   │   │   ├── common/                          # Utilidades compartidas
│   │   │   │   └── exception/                   # Manejo de excepciones
│   │   │   │       ├── GlobalExceptionHandler.java
│   │   │   │       └── ErrorResponse.java
│   │   │   │
│   │   │   └── FranquiciasApiApplication.java   # Clase principal
│   │   │
│   │   └── resources/
│   │       ├── application.yml                  # Configuración de Spring Boot
│   │       └── schema.sql                       # Script de creación de BD
│   │
│   └── test/
│       └── java/com/franquicias/                # Pruebas unitarias
│           ├── adapters/presentation/controller/
│           │   └── FranquiciaControllerTest.java
│           └── application/usecase/
│               ├── CrearFranquiciaUseCaseTest.java
│               ├── AgregarSucursalUseCaseTest.java
│               └── ModificarStockProductoUseCaseTest.java
│
├── infrastructure/
│   └── cdk/                                      # Infrastructure as Code (AWS CDK)
│       ├── bin/
│       │   └── app.ts
│       ├── lib/
│       │   └── franquicias-stack.ts
│       ├── package.json
│       └── README.md
│
├── docker-compose.yml                            # Configuración Docker Compose
├── Dockerfile                                    # Imagen Docker de la aplicación
├── pom.xml                                       # Configuración Maven
└── README.md                                     # Este archivo
```

---

## Funcionalidades Implementadas

### Funcionalidades Obligatorias

1. ✅ **Crear Franquicia**: Permite crear una nueva franquicia en el sistema
2. ✅ **Agregar Sucursal**: Permite agregar una sucursal a una franquicia existente
3. ✅ **Agregar Producto**: Permite agregar un producto a una sucursal existente
4. ✅ **Eliminar Producto**: Permite eliminar un producto de una sucursal
5. ✅ **Modificar Stock**: Permite actualizar la cantidad de stock de un producto
6. ✅ **Obtener Productos con Mayor Stock por Franquicia**: Consulta que retorna los productos con mayor stock de cada sucursal, agrupados por franquicia

### Funcionalidades Adicionales (Plus)

7. ✅ **Actualizar Nombre de Franquicia**: Permite modificar el nombre de una franquicia existente
8. ✅ **Actualizar Nombre de Sucursal**: Permite modificar el nombre de una sucursal existente
9. ✅ **Actualizar Nombre de Producto**: Permite modificar el nombre de un producto existente
10. ✅ **Manejo Global de Errores**: Captura centralizada de excepciones con respuestas consistentes
11. ✅ **Validación de Datos**: Validación automática de DTOs con Bean Validation
12. ✅ **Health Checks**: Endpoints de Actuator para verificar el estado de la aplicación
13. ✅ **Pruebas Unitarias**: Cobertura de código con pruebas unitarias para casos de uso y controladores

---

## Endpoints Principales

Base URL: `http://localhost:8080/api`

### Endpoints Obligatorios

#### 1. Crear Franquicia
```http
POST /api/franquicias
Content-Type: application/json

{
  "nombre": "Franquicia Ejemplo"
}
```

**Respuesta 201:**
```json
{
  "id": 1,
  "nombre": "Franquicia Ejemplo",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
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

**Respuesta 201:**
```json
{
  "id": 1,
  "nombre": "Sucursal Centro",
  "franquiciaId": 1,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
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

**Respuesta 201:**
```json
{
  "id": 1,
  "nombre": "Producto Ejemplo",
  "stock": 100,
  "sucursalId": 1,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

#### 4. Eliminar Producto de una Sucursal
```http
DELETE /api/sucursales/{sucursalId}/productos/{productoId}
```

**Respuesta 204:** No Content

#### 5. Modificar Stock de un Producto
```http
PUT /api/sucursales/{sucursalId}/productos/{productoId}/stock
Content-Type: application/json

{
  "stock": 150
}
```

**Respuesta 200:**
```json
{
  "id": 1,
  "nombre": "Producto Ejemplo",
  "stock": 150,
  "sucursalId": 1,
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:05:00"
}
```

#### 6. Obtener Productos con Mayor Stock por Franquicia
```http
GET /api/franquicias/{franquiciaId}/productos-maximo-stock
```

**Respuesta 200:**
```json
[
  {
    "productoId": 1,
    "productoNombre": "Producto A",
    "stock": 200,
    "sucursalId": 1,
    "sucursalNombre": "Sucursal Centro"
  },
  {
    "productoId": 2,
    "productoNombre": "Producto B",
    "stock": 150,
    "sucursalId": 2,
    "sucursalNombre": "Sucursal Norte"
  }
]
```

### Endpoints Adicionales (Plus)

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

### Endpoints de Monitoreo

#### Health Check
```http
GET /actuator/health
```

#### Info
```http
GET /actuator/info
```

---

## Requisitos Previos

Para ejecutar este proyecto necesitas tener instalado:

- **Java 21** o superior (JDK)
- **Maven 3.9+** (gestor de dependencias y construcción)
- **Docker Desktop** (opcional pero recomendado para ejecución con contenedores)
- **Docker Compose** (incluido con Docker Desktop)
- **Git** (para clonar el repositorio)

### Opcionales (solo para despliegue en AWS)

- **Node.js 18+** y **npm** (para AWS CDK)
- **AWS CLI** configurado con credenciales válidas
- **AWS CDK CLI** (`npm install -g aws-cdk`)

---

## Configuración del Proyecto

### Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd franquicias-api
```

### Verificar Requisitos

```bash
# Verificar Java
java -version  # Debe ser 21 o superior

# Verificar Maven
mvn -version   # Debe ser 3.9 o superior

# Verificar Docker (opcional)
docker --version
docker-compose --version
```

### Archivos de Configuración

La configuración principal se encuentra en `src/main/resources/application.yml`. Las propiedades pueden ser sobrescritas mediante variables de entorno.

---

## Ejecución Local

### Paso 1: Iniciar MySQL con Docker

```bash
docker run -d \
  --name mysql \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -e MYSQL_DATABASE=franquicias_db \
  -e MYSQL_USER=franquicias_user \
  -e MYSQL_PASSWORD=franquicias_pass \
  -p 3307:3306 \
  mysql:8.0
```

### Paso 2: Ejecutar el Schema SQL

```bash
# Windows (PowerShell)
Get-Content src/main/resources/schema.sql | docker exec -i mysql mysql -u franquicias_user -pfranquicias_pass franquicias_db

# Linux/Mac
mysql -u franquicias_user -pfranquicias_pass -h 127.0.0.1 -P 3307 franquicias_db < src/main/resources/schema.sql

# O usando Docker directamente
docker exec -i mysql mysql -u franquicias_user -pfranquicias_pass franquicias_db < src/main/resources/schema.sql
```

### Paso 3: Compilar y Ejecutar la Aplicación

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

O configurar las variables de entorno antes de ejecutar:

```bash
# Windows (PowerShell)
$env:SPRING_R2DBC_URL="r2dbc:mysql://localhost:3307/franquicias_db?useSSL=false&allowPublicKeyRetrieval=true"
$env:SPRING_R2DBC_USERNAME="franquicias_user"
$env:SPRING_R2DBC_PASSWORD="franquicias_pass"
mvn spring-boot:run

# Linux/Mac
export SPRING_R2DBC_URL=r2dbc:mysql://localhost:3307/franquicias_db?useSSL=false&allowPublicKeyRetrieval=true
export SPRING_R2DBC_USERNAME=franquicias_user
export SPRING_R2DBC_PASSWORD=franquicias_pass
mvn spring-boot:run
```

### Paso 4: Verificar que la Aplicación Está Ejecutándose

```bash
# Verificar health check
curl http://localhost:8080/actuator/health

# O abrir en el navegador
# http://localhost:8080/actuator/health
```

La aplicación estará disponible en: `http://localhost:8080`

---

## Ejecución con Docker

Esta es la forma más sencilla de ejecutar el proyecto completo.

### Paso 1: Asegurar que Docker Desktop Está Ejecutándose

### Paso 2: Ejecutar con Docker Compose

```bash
docker-compose up -d
```

Este comando:
- Construye la imagen Docker de la aplicación
- Inicia un contenedor MySQL con la base de datos
- Inicia el contenedor de la aplicación
- Configura la red entre contenedores automáticamente
- Ejecuta el schema SQL automáticamente en MySQL

### Paso 3: Verificar que los Contenedores Están Ejecutándose

```bash
docker ps
```

Deberías ver dos contenedores:
- `franquicias-mysql` (MySQL 8.0)
- `franquicias-api` (Aplicación Spring Boot)

### Paso 4: Ver Logs (Opcional)

```bash
# Ver logs de la aplicación
docker-compose logs -f app

# Ver logs de MySQL
docker-compose logs -f mysql

# Ver todos los logs
docker-compose logs -f
```

### Paso 5: Detener los Contenedores

```bash
# Detener y eliminar contenedores
docker-compose down

# Detener y eliminar contenedores + volúmenes (limpia la BD)
docker-compose down -v
```

### Paso 6: Reconstruir la Imagen (si hay cambios)

```bash
docker-compose up -d --build
```

La aplicación estará disponible en: `http://localhost:8080`

---

## Variables de Entorno

Las siguientes variables pueden ser configuradas para personalizar el comportamiento de la aplicación:

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SPRING_R2DBC_URL` | URL de conexión R2DBC a MySQL | `r2dbc:mysql://localhost:3306/franquicias_db?useSSL=false&allowPublicKeyRetrieval=true` |
| `SPRING_R2DBC_USERNAME` | Usuario de la base de datos | `franquicias_user` |
| `SPRING_R2DBC_PASSWORD` | Contraseña de la base de datos | `franquicias_pass` |
| `DB_USERNAME` | Alias para username (compatibilidad) | `franquicias_user` |
| `DB_PASSWORD` | Alias para password (compatibilidad) | `franquicias_pass` |
| `DB_POOL_INITIAL_SIZE` | Tamaño inicial del pool de conexiones | `5` |
| `DB_POOL_MAX_SIZE` | Tamaño máximo del pool de conexiones | `10` |
| `DB_POOL_MAX_IDLE_TIME` | Tiempo máximo de inactividad de conexiones | `30m` |
| `SERVER_PORT` | Puerto del servidor HTTP | `8080` |
| `APP_NAME` | Nombre de la aplicación | `franquicias-api` |
| `SPRING_SQL_INIT_MODE` | Modo de inicialización de SQL | `never` |
| `LOG_LEVEL_ROOT` | Nivel de logging raíz | `INFO` |
| `LOG_LEVEL_APP` | Nivel de logging de la aplicación (`com.franquicias`) | `DEBUG` |
| `LOG_LEVEL_R2DBC` | Nivel de logging de R2DBC | `INFO` |
| `LOG_LEVEL_SPRING_R2DBC` | Nivel de logging de Spring R2DBC | `INFO` |

### Ejemplo de Uso

```bash
# Windows (PowerShell)
$env:SERVER_PORT="9090"
$env:LOG_LEVEL_APP="INFO"
mvn spring-boot:run

# Linux/Mac
export SERVER_PORT=9090
export LOG_LEVEL_APP=INFO
mvn spring-boot:run
```

---

## Despliegue en la Nube

El proyecto incluye configuración para despliegue en **AWS EC2** utilizando **AWS CDK (TypeScript)**.

### Prerrequisitos para Despliegue en AWS

- **AWS CLI** configurado con credenciales válidas
- **Node.js 18+** y **npm** instalados
- **AWS CDK CLI** instalado globalmente: `npm install -g aws-cdk`
- Cuenta de AWS con permisos adecuados (EC2, VPC, IAM, CloudWatch)

### Pasos para Desplegar en AWS

1. **Navegar al directorio de CDK:**

```bash
cd infrastructure/cdk
```

2. **Instalar dependencias de Node.js:**

```bash
npm install
```

3. **Compilar el proyecto TypeScript:**

```bash
npm run build
```

4. **Bootstrap CDK (solo la primera vez):**

```bash
cdk bootstrap
```

Este comando crea los recursos necesarios en AWS para almacenar artefactos de CDK.

5. **Revisar los recursos que se van a crear:**

```bash
cdk diff
```

6. **Desplegar la infraestructura:**

```bash
cdk deploy
```

Este comando:
- Crea una VPC en AWS
- Crea una instancia EC2 con Amazon Linux 2023
- Configura Security Groups para permitir tráfico HTTP (puerto 8080)
- Configura IAM Roles para la instancia EC2
- Configura CloudWatch Logs
- Despliega la aplicación en la instancia EC2

### Configuración de la Base de Datos en AWS

En el despliegue actual, la base de datos MySQL se ejecuta en un contenedor Docker dentro de la instancia EC2. No se utiliza RDS para mantener la solución simple y alineada al alcance de la prueba técnica.

**Resumen de despliegue:**
- **Local**: MySQL en contenedor Docker (docker-compose)
- **AWS**: MySQL en contenedor Docker dentro de EC2

Esto permite una configuración consistente entre entornos y facilita el despliegue sin la complejidad adicional de gestionar una instancia RDS separada.

7. **Obtener la URL de la API:**

```bash
aws cloudformation describe-stacks \
  --stack-name FranquiciasApiStack \
  --query "Stacks[0].Outputs[?OutputKey=='ApiUrl'].OutputValue" \
  --output text
```

O consultar en la consola de AWS CloudFormation.

### Destruir la Infraestructura

```bash
cdk destroy
```

⚠️ **Nota**: Esto eliminará todos los recursos creados, incluyendo la instancia EC2 y la VPC.

### Documentación Adicional

Para más detalles sobre la infraestructura, consulta:
- [`infrastructure/cdk/README.md`](infrastructure/cdk/README.md) - Documentación completa de IaC
- [`infrastructure/cdk/SOLUCION_PERMISOS.md`](infrastructure/cdk/SOLUCION_PERMISOS.md) - Guía para resolver problemas de permisos

---

## API Desplegada (Ambiente de Pruebas)

La API se encuentra desplegada en AWS EC2 y accesible públicamente en:

**Base URL:**
```
http://100.48.214.127:8080/api
```

⚠️ **Nota**: La instancia puede ser detenida después del proceso de evaluación para evitar costos innecesarios.

---

## Pruebas Unitarias

El proyecto incluye pruebas unitarias para casos de uso y controladores, utilizando JUnit 5, Mockito y StepVerifier para testing reactivo.

### Ejecutar Todas las Pruebas

```bash
mvn test
```

### Ejecutar Pruebas con Reporte de Cobertura

```bash
mvn clean test jacoco:report
```

El reporte de cobertura estará disponible en:
```
target/site/jacoco/index.html
```

Abre este archivo en un navegador para ver el reporte detallado de cobertura.

### Verificar Cobertura Mínima

```bash
mvn clean verify
```

Este comando ejecuta las pruebas y verifica que la cobertura sea al menos del 80% en líneas y branches. Si no se cumple, la construcción falla.

### Pruebas Incluidas

- **CrearFranquiciaUseCaseTest**: Prueba la creación de franquicias con validaciones
- **AgregarSucursalUseCaseTest**: Prueba la adición de sucursales
- **ModificarStockProductoUseCaseTest**: Prueba la modificación de stock
- **FranquiciaControllerTest**: Pruebas de integración del controlador

### Cobertura de Código

El proyecto está configurado para requerir una cobertura mínima del **80%** en:
- Líneas de código (LINE)
- Ramas condicionales (BRANCH)

Esta configuración se encuentra en `pom.xml` con el plugin JaCoCo.

---

## Decisiones Técnicas

### 1. Clean Architecture

**Decisión**: Implementar Clean Architecture con separación en capas (Domain, Application, Infrastructure, Adapters).

**Razón**: 
- Facilita el mantenimiento y testing
- Permite cambiar tecnologías (BD, frameworks) sin afectar la lógica de negocio
- Hace el código más legible y escalable
- Demuestra conocimiento de arquitectura de software

### 2. Programación Reactiva con Spring WebFlux

**Decisión**: Usar Spring WebFlux en lugar de Spring MVC tradicional.

**Razón**:
- Mejor rendimiento en aplicaciones con muchas conexiones concurrentes
- No bloquea hilos (modelo event-loop)
- Permite manejar más peticiones con menos recursos
- Demuestra conocimiento de programación reactiva

### 3. R2DBC en lugar de JPA/Hibernate

**Decisión**: Usar R2DBC para acceso a datos en lugar de JPA/Hibernate.

**Razón**:
- JPA/Hibernate es bloqueante por naturaleza
- R2DBC es completamente reactivo y no bloqueante
- Permite aprovechar al máximo las ventajas de WebFlux
- Mantiene la consistencia reactiva en toda la aplicación

### 4. MySQL como Base de Datos

**Decisión**: Usar MySQL 8.0 como base de datos relacional.

**Razón**:
- Ampliamente usado en la industria
- Driver R2DBC disponible y estable
- Soporta todas las funcionalidades necesarias
- Fácil de contenedorizar con Docker

### 5. Docker y Docker Compose

**Decisión**: Contenedorizar completamente la aplicación con Docker.

**Razón**:
- Facilita el despliegue y desarrollo
- Garantiza consistencia entre entornos
- Permite aislamiento de dependencias
- Simplifica la configuración local

### 6. AWS CDK para Infrastructure as Code

**Decisión**: Usar AWS CDK en TypeScript para definir la infraestructura.

**Razón**:
- Permite versionar la infraestructura
- Facilita el despliegue repetible
- TypeScript proporciona type safety
- Demuestra conocimiento de DevOps e IaC

### 7. JaCoCo para Cobertura de Código

**Decisión**: Configurar JaCoCo con un mínimo del 80% de cobertura.

**Razón**:
- Garantiza calidad del código
- Detecta código no testeado
- Estándar en la industria
- Muestra compromiso con la calidad

### 8. Lombok para Reducir Boilerplate

**Decisión**: Incluir Lombok para generar código automáticamente (getters, setters, constructors, etc.).

**Razón**:
- Reduce la cantidad de código repetitivo
- Hace el código más legible
- Mantiene las clases enfocadas en la lógica

### 9. Spring Boot Actuator

**Decisión**: Incluir Spring Boot Actuator para health checks.

**Razón**:
- Proporciona endpoints de monitoreo estándar
- Útil para orquestadores de contenedores (Kubernetes, ECS)
- Facilita el diagnóstico de problemas

### 10. Validación con Bean Validation

**Decisión**: Usar Bean Validation (javax.validation) para validar DTOs.

**Razón**:
- Validación declarativa y estándar
- Reduce código de validación manual
- Mensajes de error consistentes

---

## Suposiciones y Consideraciones

### Suposiciones de Negocio

1. **Unicidad de nombres**: Los nombres de franquicias son únicos a nivel global, y los nombres de sucursales son únicos dentro de cada franquicia. De igual manera, los nombres de productos son únicos dentro de cada sucursal. Estas restricciones se implementan a nivel de base de datos mediante índices únicos:
   - `uk_franquicia_nombre` para nombres de franquicias
   - `uk_sucursal_franquicia_nombre` (índice compuesto) para nombres de sucursales por franquicia
   - `uk_producto_sucursal_nombre` (índice compuesto) para nombres de productos por sucursal

2. **Stock mínimo**: No se valida un stock mínimo (puede ser negativo en la lógica actual, aunque se valida que no sea null).

3. **Eliminación en cascada**: Se implementó eliminación en cascada a nivel de base de datos mediante `ON DELETE CASCADE` en las foreign keys. Esto significa que al eliminar una franquicia, se eliminan automáticamente todas sus sucursales, y al eliminar una sucursal, se eliminan automáticamente todos sus productos.

4. **Transacciones**: No se implementaron transacciones reactivas explícitas con `TransactionalOperator`. Cada operación persiste una sola entidad, por lo que no se requieren transacciones compuestas en el alcance actual del proyecto.

5. **Autenticación/Autorización**: No se implementó sistema de autenticación ni autorización, asumiendo que es una API interna o que se manejará a nivel de gateway/proxy.

6. **Auditoría**: Los campos `createdAt` y `updatedAt` se manejan a nivel de aplicación, no automáticamente por la BD.

### Consideraciones Técnicas

1. **Escalabilidad**: La aplicación está diseñada para escalar horizontalmente, ya que no hay estado compartido entre instancias.

2. **Rendimiento**: Se optimizó para alto rendimiento con programación reactiva, pero no se implementaron cachés (Redis, etc.).

3. **Monitoreo**: Se incluye Actuator básico, pero no se implementó integración completa con sistemas de monitoreo (Prometheus, Grafana, etc.).

4. **Logging**: Se usa logging básico de Spring Boot, no logging estructurado (JSON) ni agregación centralizada.

5. **Versionado de API**: No se implementó versionado de API (v1, v2, etc.), asumiendo que es la primera versión.

6. **Documentación de API**: No se incluyó OpenAPI/Swagger, aunque podría agregarse fácilmente.

7. **CORS**: No se configuró CORS, asumiendo que la API se consumirá desde el mismo dominio o se configurará en un gateway.

8. **Rate Limiting**: No se implementó rate limiting, asumiendo que se manejará a nivel de infraestructura.

---

## Limitaciones Conocidas

### Limitaciones Funcionales

1. **Sin paginación**: Los endpoints que retornan listas (como productos con mayor stock) no implementan paginación. En producción, debería implementarse para grandes volúmenes de datos.

2. **Sin búsqueda/filtrado**: No hay endpoints de búsqueda o filtrado de franquicias, sucursales o productos.

3. **Sin ordenamiento configurable**: Las consultas tienen ordenamiento fijo.

4. **Sin soft delete**: Las eliminaciones son físicas (hard delete). No se implementó soft delete con campos de auditoría.

5. **Sin historial de cambios**: No se registra el historial de cambios en stock, nombres, etc.

6. **Sin validación de relaciones**: No se valida explícitamente que una sucursal pertenezca a una franquicia al agregar productos (se asume que los IDs son correctos).

### Limitaciones Técnicas

1. **Cobertura de pruebas**: Aunque se implementaron pruebas unitarias, no todas las clases tienen cobertura completa (repositorios, mappers, algunos controladores).

2. **Pruebas de integración**: No se implementaron pruebas de integración end-to-end, solo pruebas unitarias y algunos tests de controladores.

3. **Manejo de errores**: Aunque existe manejo global de errores, no todos los casos de error están cubiertos (por ejemplo, errores de conexión a BD en tiempo de ejecución).

4. **Configuración de pool**: El pool de conexiones R2DBC tiene configuración básica, podría optimizarse según la carga esperada.

5. **Sin retry logic**: No se implementó lógica de reintentos para operaciones que fallan transitoriamente.

6. **Sin circuit breaker**: No se implementó circuit breaker para proteger la aplicación de fallos en cascada.

### Limitaciones de Infraestructura

1. **Despliegue en AWS**: El despliegue con CDK es básico, no incluye:
   - Load Balancer
   - Auto Scaling Groups
   - Base de datos RDS gestionada (MySQL se ejecuta en contenedor Docker dentro de EC2)
   - CDN (CloudFront)
   - WAF

2. **Sin CI/CD**: No se incluyó pipeline de CI/CD (GitHub Actions, GitLab CI, Jenkins, etc.).

3. **Sin secrets management**: Las credenciales se manejan como variables de entorno, no con un servicio de gestión de secretos (AWS Secrets Manager, HashiCorp Vault).

---

## Posibles Mejoras

### Mejoras Funcionales

1. **Paginación y ordenamiento**: Implementar paginación y ordenamiento configurable en todos los endpoints que retornan listas.

2. **Búsqueda y filtrado**: Agregar endpoints de búsqueda con filtros avanzados (por nombre, rango de fechas, etc.).

3. **Soft Delete**: Implementar eliminación lógica con campos de auditoría (`deletedAt`, `deletedBy`).

4. **Auditoría completa**: Agregar campos de auditoría (`createdBy`, `updatedBy`) y historial de cambios.

5. **Validaciones de negocio**: Implementar validaciones más robustas (stock mínimo, nombres únicos por contexto, etc.).

6. **Endpoints adicionales**: 
   - Listar todas las franquicias
   - Listar sucursales de una franquicia
   - Listar productos de una sucursal
   - Obtener detalles de una entidad por ID

7. **Reportes**: Implementar endpoints para reportes agregados (stock total por franquicia, productos más vendidos, etc.).

### Mejoras Técnicas

1. **Caché**: Implementar caché (Redis) para consultas frecuentes.

2. **Pruebas**: 
   - Aumentar cobertura de pruebas al 90%+
   - Agregar pruebas de integración end-to-end
   - Implementar pruebas de carga (JMeter, Gatling)

3. **Documentación de API**: Agregar OpenAPI/Swagger con anotaciones detalladas.

4. **Logging estructurado**: Implementar logging en formato JSON con correlación de requests (traceId, spanId).

5. **Métricas**: Integrar Prometheus y Grafana para monitoreo y alertas.

6. **Manejo de errores**: Mejorar manejo de errores con códigos de error personalizados y mensajes más descriptivos.

7. **Versionado de API**: Implementar versionado de API (v1, v2) para mantener compatibilidad.

8. **Retry y Circuit Breaker**: Implementar patrones de resiliencia (Resilience4j).

9. **Validación avanzada**: Implementar validaciones personalizadas y grupos de validación.

10. **Optimización de consultas**: Revisar y optimizar consultas SQL, agregar índices necesarios.

### Mejoras de Infraestructura

1. **Base de datos gestionada**: Migrar a RDS o Aurora MySQL en lugar de MySQL en contenedor Docker dentro de EC2. Esto proporcionaría alta disponibilidad, backups automáticos y gestión administrada por AWS.

2. **Load Balancer**: Agregar Application Load Balancer en AWS para distribuir carga.

3. **Auto Scaling**: Implementar Auto Scaling Groups para escalar automáticamente según la carga.

4. **CI/CD**: Implementar pipeline completo (build, test, deploy) con GitHub Actions, GitLab CI o Jenkins.

5. **Secrets Management**: Integrar AWS Secrets Manager o HashiCorp Vault para gestión de credenciales.

6. **CDN**: Configurar CloudFront para cachear respuestas estáticas y mejorar latencia.

7. **WAF**: Implementar AWS WAF para protección contra ataques comunes.

8. **Backup automático**: Configurar backups automáticos de la base de datos.

9. **Multi-región**: Diseñar arquitectura multi-región para alta disponibilidad.

10. **Container Orchestration**: Migrar a Kubernetes (EKS) o ECS para orquestación avanzada de contenedores.

### Mejoras de Seguridad

1. **Autenticación y Autorización**: Implementar OAuth2/JWT o API Keys para autenticación.

2. **HTTPS**: Configurar SSL/TLS con certificados válidos.

3. **Rate Limiting**: Implementar rate limiting (Spring Cloud Gateway, AWS API Gateway).

4. **Validación de input**: Implementar sanitización de inputs para prevenir inyecciones.

5. **CORS configurado**: Configurar CORS apropiadamente según el origen de las peticiones.

---

## Autor / Contacto

**Proyecto desarrollado como prueba técnica**

Para consultas o más información sobre este proyecto, puedes contactar a través de:

- **Repositorio**: [URL del repositorio]
- **Email**: [Email de contacto]
- **LinkedIn**: [Perfil de LinkedIn]

---

## Licencia

Este proyecto es una **prueba técnica** de desarrollo backend y está destinado únicamente para fines de evaluación y demostración de habilidades técnicas.

---

## Notas Finales

Este proyecto demuestra conocimientos en:

- ✅ Programación reactiva con Spring WebFlux
- ✅ Clean Architecture y principios SOLID
- ✅ Acceso a datos reactivo con R2DBC
- ✅ Testing unitario con herramientas modernas
- ✅ Contenedorización con Docker
- ✅ Infrastructure as Code con AWS CDK
- ✅ Buenas prácticas de desarrollo de software

**Última actualización**: Enero 2024
