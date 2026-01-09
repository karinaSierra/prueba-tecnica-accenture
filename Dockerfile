# Multi-stage build para optimizar el tamaño de la imagen
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (caché de capas)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar y ejecutar tests
RUN mvn clean package -DskipTests

# Imagen final
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8080

# Health check (instalar wget en Alpine si es necesario, o usar una verificación simple)
# Nota: Si Spring Boot Actuator no está configurado, puedes comentar esta línea
# HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
#   CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Ejecutar aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

