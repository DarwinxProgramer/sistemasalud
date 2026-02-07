// src/db/db.ts
// IndexedDB Database Schema for HCE Offline-First

import Dexie, { type Table } from 'dexie';
import type { Paciente } from '../models/Paciente';

// Definición de tipos para las tablas adicionales
export interface SyncQueueItem {
    id?: number;
    type: 'CREATE' | 'UPDATE' | 'DELETE';
    entity: 'paciente' | 'consulta' | 'antecedente';
    data: any;
    timestamp: number;
    synced: number; // 0 = false, 1 = true (Dexie no puede indexar booleanos)
    retries: number;
}

export interface CatalogoItem {
    id?: number;
    tipo: string; // 'provincia', 'canton', 'parroquia', 'enfermedad', etc.
    codigo?: string;
    nombre: string;
    parentId?: number; // Para relaciones jerárquicas
    metadatos?: any;
}

export interface AppMetadata {
    key: string;
    value: any;
    updatedAt: number;
}

// Clase principal de la base de datos
class HceDatabase extends Dexie {
    // Declaramos las tablas como propiedades con su tipo
    pacientes!: Table<Paciente, string>; // Key: uuidOffline (string)
    syncQueue!: Table<SyncQueueItem, number>;
    catalogos!: Table<CatalogoItem, number>;
    metadata!: Table<AppMetadata, string>;

    constructor() {
        super('HceDatabaseV2');

        // Definir el esquema de la BD (versión 2 - cambio de PK a uuidOffline)
        this.version(2).stores({
            // Tabla de pacientes: indexada por 'uuidOffline' (PK), búsqueda por cedula e idPaciente
            pacientes: 'uuidOffline, cedula, idPaciente, nombres, apellidos, provincia, canton',

            // Cola de sincronización: auto-increment ID, indexada por estado 'synced'
            syncQueue: '++id, synced, timestamp, entity',

            // Catálogos (provincias, cantones, enfermedades, etc.)
            catalogos: '++id, tipo, codigo, nombre, parentId',

            // Metadata de la app (última sincronización, versión de datos, etc.)
            metadata: 'key'
        });
    }
}

// Instancia única de la base de datos (singleton)
export const db = new HceDatabase();

// Helper functions para operaciones comunes
export const dbHelpers = {
    // Obtener todos los pacientes
    async getAllPacientes(): Promise<Paciente[]> {
        return await db.pacientes.toArray();
    },

    // Buscar paciente por cédula
    async getPacienteByCedula(cedula: string): Promise<Paciente | undefined> {
        return await db.pacientes.get(cedula);
    },

    // Guardar o actualizar paciente
    async savePaciente(paciente: Paciente): Promise<string> {
        if (!paciente.uuidOffline) {
            paciente.uuidOffline = crypto.randomUUID();
        }
        return await db.pacientes.put(paciente);
    },

    // Eliminar paciente
    async deletePaciente(cedula: string): Promise<void> {
        // Ahora cedula no es PK, buscamos primero
        const paciente = await db.pacientes.where('cedula').equals(cedula).first();
        if (paciente && paciente.uuidOffline) {
            await db.pacientes.delete(paciente.uuidOffline);
        }
    },

    // Agregar item a la cola de sincronización
    async addToSyncQueue(item: Omit<SyncQueueItem, 'id' | 'timestamp' | 'synced' | 'retries'>): Promise<number> {
        return await db.syncQueue.add({
            ...item,
            timestamp: Date.now(),
            synced: 0, // 0 = false
            retries: 0
        });
    },

    // Obtener items pendientes de sincronización
    async getPendingSyncItems(): Promise<SyncQueueItem[]> {
        return await db.syncQueue
            .where('synced')
            .equals(0)
            .sortBy('timestamp');
    },

    // Marcar item como sincronizado
    async markAsSynced(itemId: number): Promise<void> {
        await db.syncQueue.update(itemId, { synced: 1 }); // 1 = true
    },

    // Limpiar items sincronizados (opcional, para no llenar la BD)
    async clearSyncedItems(): Promise<void> {
        await db.syncQueue.where('synced').equals(1).delete();
    },

    // Guardar metadata
    async setMetadata(key: string, value: any): Promise<void> {
        await db.metadata.put({ key, value, updatedAt: Date.now() });
    },

    // Obtener metadata
    async getMetadata(key: string): Promise<any> {
        const item = await db.metadata.get(key);
        return item?.value;
    }
};
