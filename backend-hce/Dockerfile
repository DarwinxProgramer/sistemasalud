# ========================================
# Dockerfile MULTI-STAGE
# 1. Compila el código con Maven
# 2. Crea imagen ligera para producción
# ========================================

# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copiar configuración de dependencias
COPY pom.xml .

# Descargar dependencias (cache layer)
RUN mvn dependency:go-offline

# Copiar código fuente
COPY src ./src

# Compilar y empaquetar (saltando tests para velocidad)
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Crear usuario seguro
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar JAR desde el stage de compilación
COPY --from=builder /app/target/*.jar app.jar

# Configuración
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
