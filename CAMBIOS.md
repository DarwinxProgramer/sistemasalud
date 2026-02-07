# Plan de Implementación: Sincronización de Consultas Médicas

Este documento detalla los cambios necesarios para habilitar la sincronización bidireccional (Offline-First) de las consultas médicas.

## 📁 Estructura y Ubicación de Archivos

A continuación se listan los archivos que serán modificados o creados.

### Backend (Java Spring Boot)
Ruta Base: `c:\Users\darwi\Desktop\Universidad\Proyecto Cato\backend-hce\src\main\java\ec\gob\salud\hce\backend`

| Tipo | Archivo | Ubicación Relativa | Descripción del Cambio |
|------|---------|-------------------|------------------------|
| **Entidad** | `Consulta.java` | `\entity\Consulta.java` | Agregar campos `uuidOffline`, `syncStatus`, `lastModified`, `origin`. |
| **DTO** | `ConsultaDTO.java` | `\dto\ConsultaDTO.java` | Incluir `uuidOffline` para transferencia de datos. |
| **Repositorio** | `ConsultaRepository.java` | `\repository\ConsultaRepository.java` | Añadir método `findByUuidOffline`. |
| **Mapper** | `ConsultaMapper.java` | `\mapper\ConsultaMapper.java` | Mapear los nuevos campos entre DTO y Entidad. |
| **Servicio** | `ConsultaService.java` | `\service\ConsultaService.java` | Lógica para guardar/actualizar buscando por UUID. |
| **Servicio Sync** | `SyncService.java` | `\service\SyncService.java` | Procesar objetos tipo `"consulta"` en `procesarSubida`. |

### Frontend (React + TypeScript)
Ruta Base: `c:\Users\darwi\Desktop\Universidad\Proyecto Cato\hce_frontend\src`

| Tipo | Archivo | Ubicación Relativa | Descripción del Cambio |
|------|---------|-------------------|------------------------|
| **Servicio** | `syncService.ts` | `\services\syncService.ts` | Manejar respuesta de sincronización para actualizar IDs de consultas. |
| **Mapper** | `consultaMapper.ts` | `\services\consultaMapper.ts` | Asegurar que `uuidOffline` se preserve al convertir formatos. |

---

## 📝 Detalle de Cambios

### 1. Backend: Actualización del Modelo de Datos

#### `Consulta.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\entity\Consulta.java`
Agregar los siguientes campos para el manejo offline:
```java
@Column(name = "uuid_offline")
private String uuidOffline;

@Column(name = "sync_status")
private String syncStatus;

@Column(name = "last_modified")
private LocalDateTime lastModified;

@Column(name = "origin")
private String origin;
```

#### `ConsultaDTO.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\dto\ConsultaDTO.java`
Agregar campo para recibir el UUID desde el frontend:
```java
private String uuidOffline;
```

### 2. Backend: Lógica de Persistencia

#### `ConsultaRepository.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\repository\ConsultaRepository.java`
Declarar método de búsqueda:
```java
Optional<Consulta> findByUuidOffline(String uuidOffline);
```

#### `ConsultaService.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\service\ConsultaService.java`
Implementar lógica `guardarSincronizado`:
1.  Buscar si existe consulta con `uuidOffline`.
2.  Si existe: Actualizar registro.
3.  Si no existe: Crear nuevo registro.

#### `ConsultaMapper.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\mapper\ConsultaMapper.java`
Actualizar `toEntity` y `toDto` para transferir `uuidOffline`.

### 3. Backend: Servicio de Sincronización

#### `SyncService.java`
**Ubicación:** `backend-hce\src\main\java\ec\gob\salud\hce\backend\service\SyncService.java`
En el método `procesarSubida`, agregar el caso para consultas:
```java
if ("consulta".equalsIgnoreCase(request.getEntity())) {
    // Convertir data a ConsultaDTO
    // Llamar a ConsultaService.guardarSincronizado
    // Retornar mapeo (UUID -> ID Base Datos)
}
```

### 4. Frontend: Ajustes de Sincronización

#### `consultaMapper.ts`
**Ubicación:** `hce_frontend\src\services\consultaMapper.ts`
Asegurar que el campo `id` del frontend (que suele ser el UUID temporal) se envíe como `uuidOffline` al backend.

#### `syncService.ts`
**Ubicación:** `hce_frontend\src\services\syncService.ts`
En `syncUp()`:
1.  Detectar respuesta de mapeo para entidad `"consulta"`.
2.  Buscar al paciente dueño de la consulta en IndexedDB.
3.  Actualizar el ID real de la consulta dentro del array `historiaClinica`.
4.  Guardar el paciente actualizado.

## ✅ Plan de Verificación

1.  **Prueba Offline:** Crear consulta sin internet -> Verificar UUID en IndexedDB.
2.  **Prueba Sync Up:** Conectar internet -> Verificar que se envía al backend y se recibe ID real.
3.  **Prueba Persistencia:** Verificar en MySQL que `uuid_offline` y `sync_status` se guardaron correctamente.
