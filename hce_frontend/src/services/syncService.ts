// src/services/syncService.ts
import { db, dbHelpers } from '../db/db';

// Corrección para variables de entorno en Vite
const API_BASE_URL = (import.meta as any).env?.VITE_API_URL || '/api';

export interface SyncStatus {
    lastSync: number | null;
    syncing: boolean;
    pendingChanges: number;
    online: boolean;
}

type ToastCallback = (message: string, type?: 'info' | 'success' | 'warning' | 'error') => void;

class SyncService {
    private syncInProgress = false;
    private listeners: Array<(status: SyncStatus) => void> = [];
    private toastCallback: ToastCallback | null = null;
    private wasOffline = false;

    setToastCallback(callback: ToastCallback) {
        this.toastCallback = callback;
    }

    private showToast(message: string, type?: 'info' | 'success' | 'warning' | 'error') {
        if (this.toastCallback) {
            this.toastCallback(message, type);
        }
    }

    async getStatus(): Promise<SyncStatus> {
        const lastSync = await dbHelpers.getMetadata('lastSyncTimestamp');
        const pendingItems = await dbHelpers.getPendingSyncItems();

        return {
            lastSync: lastSync || null,
            syncing: this.syncInProgress,
            pendingChanges: pendingItems.length,
            online: navigator.onLine
        };
    }

    subscribe(callback: (status: SyncStatus) => void): () => void {
        this.listeners.push(callback);
        return () => {
            this.listeners = this.listeners.filter(l => l !== callback);
        };
    }

    private async notifyListeners() {
        const status = await this.getStatus();
        this.listeners.forEach(listener => listener(status));
    }

    /**
     * Sincronización de bajada: Descargar datos del servidor
     */
    async syncDown(): Promise<void> {
        if (!navigator.onLine) {
            console.warn('[SyncService] Sin conexión, skip sync down');
            return;
        }

        try {
            this.syncInProgress = true;
            await this.notifyListeners();
            this.showToast('📥 Sincronizando datos...', 'info');

            const response = await fetch(`${API_BASE_URL}/sync/down`, {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();

            // 1. PROCESAR PACIENTES
            if (data.pacientes && Array.isArray(data.pacientes)) {
                const pacientesParaGuardar = data.pacientes.map((p: any) => {
                    // Unir nombres y apellidos si vienen separados
                    const nombres = [p.primerNombre, p.segundoNombre].filter(Boolean).join(' ').trim();
                    const apellidos = [p.apellidoPaterno, p.apellidoMaterno].filter(Boolean).join(' ').trim();

                    // Mapeo de tutor
                    let filiacion = null;
                    if (p.tutor) {
                        filiacion = {
                            nombreResponsable: [p.tutor.primerNombre, p.tutor.segundoNombre, p.tutor.primerApellido, p.tutor.segundoApellido].filter(Boolean).join(' ').trim(),
                            parentesco: p.tutor.parentesco,
                            telefonoContacto: p.tutor.telefono,
                            nivelEducativoResponsable: p.tutor.nivelEducativo,
                            domicilioActual: p.tutor.direccion,
                            parroquia: p.tutor.idParroquia
                        };
                    }

                    return {
                        ...p,
                        id: p.idPaciente, // ID Dexie = ID Backend
                        cedula: p.cedula,
                        nombres,
                        apellidos,
                        filiacion,
                        historiaClinica: p.historiaClinica || [], 
                        uuidOffline: p.uuidOffline || self.crypto.randomUUID()
                    };
                });

                // Guardar sin borrar todo (bulkPut hace upsert)
                await (db as any).transaction('rw', db.pacientes, async () => {
                    await db.pacientes.bulkPut(pacientesParaGuardar);
                });
            }

            // 2. PROCESAR CONSULTAS
            if (data.consultas && Array.isArray(data.consultas)) {
                const { mapConsultaBackendToFrontend } = await import('./consultaMapper');

                for (const consultaBackend of data.consultas) {
                    // Forzar conversión a número para asegurar coincidencia
                    const idPacienteTarget = Number(consultaBackend.idPaciente);

                    // Búsqueda flexible: intentamos por ID directo (con 'as any' para calmar a TS)
                    let paciente: any = await db.pacientes.get(idPacienteTarget as any);

                    // Si no lo encuentra, intentamos filtrar por la propiedad idPaciente
                    if (!paciente) {
                        paciente = await db.pacientes
                            .filter((p: any) => Number(p.idPaciente) === idPacienteTarget)
                            .first();
                    }

                    if (paciente) {
                        if (!paciente.historiaClinica) paciente.historiaClinica = [];
                        
                        const consultaFrontend = mapConsultaBackendToFrontend(consultaBackend);
                        
                        // Evitar duplicados
                        const existe = paciente.historiaClinica.some((c: any) => 
                            c.id === consultaFrontend.id || c.id === consultaBackend.uuidOffline
                        );

                        if (!existe) {
                            paciente.historiaClinica.push(consultaFrontend);
                            await db.pacientes.put(paciente);
                            console.log(`[Sync] Consulta ${consultaFrontend.id} agregada a paciente ${idPacienteTarget}`);
                        }
                    } else {
                        console.warn(`[Sync] Paciente local NO encontrado para consulta. ID Buscado: ${idPacienteTarget}`);
                    }
                }
            }

            await dbHelpers.setMetadata('lastSyncTimestamp', Date.now());

        } catch (error) {
            console.error('[SyncService] Error en sync down:', error);
            // Ignoramos error 500 en UI para no spamear
            if (!String(error).includes('500')) {
                this.showToast('⚠️ Error al sincronizar', 'warning');
            }
        } finally {
            this.syncInProgress = false;
            await this.notifyListeners();
        }
    }

    /**
     * Sincronización de subida
     */
    async syncUp(): Promise<void> {
        if (!navigator.onLine) return;

        const pendingItems = await dbHelpers.getPendingSyncItems();
        if (pendingItems.length === 0) return;

        try {
            this.syncInProgress = true;
            await this.notifyListeners();

            const totalItems = pendingItems.length;
            this.showToast(`🔄 Subiendo ${totalItems} cambios...`, 'info');

            let syncedCount = 0;

            for (const item of pendingItems) {
                try {
                    const response = await fetch(`${API_BASE_URL}/sync/up`, {
                        method: 'POST',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(item)
                    });

                    if (response.ok) {
                        const mappings = await response.json();

                        if (mappings && Array.isArray(mappings)) {
                            for (const map of mappings) {
                                // Actualizar IDs de Pacientes
                                if (map.entityType === 'paciente' && map.uuidOffline && map.newId) {
                                    await db.pacientes.update(map.uuidOffline, { idPaciente: map.newId });
                                }

                                // Actualizar IDs de Consultas
                                if (map.entityType === 'consulta' && map.uuidOffline && map.newId) {
                                    const paciente = await db.pacientes
                                        .filter((p: any) => p.historiaClinica?.some((c: any) => c.id === map.uuidOffline))
                                        .first();

                                    if (paciente && paciente.historiaClinica) {
                                        paciente.historiaClinica = paciente.historiaClinica.map((c: any) => {
                                            if (c.id === map.uuidOffline) {
                                                return { ...c, id: map.newId, idConsulta: map.newId };
                                            }
                                            return c;
                                        });
                                        await db.pacientes.put(paciente);
                                    }
                                }
                            }
                        }

                        await dbHelpers.markAsSynced(item.id!);
                        syncedCount++;
                    }
                } catch (error) {
                    console.error(`[SyncService] Error item ${item.id}:`, error);
                    await (db as any).syncQueue.update(item.id!, { retries: (item.retries || 0) + 1 });
                }
            }

            await dbHelpers.clearSyncedItems();
            if (syncedCount > 0) this.showToast(`✅ ${syncedCount} cambios subidos`, 'success');

        } catch (error) {
            console.error('[SyncService] Error sync up:', error);
        } finally {
            this.syncInProgress = false;
            await this.notifyListeners();
        }
    }

    async sync(): Promise<void> {
        if (this.syncInProgress) return;
        try {
            await this.syncUp();
            await this.syncDown();
        } catch (error) {
            console.error('[SyncService] Error en sync:', error);
        }
    }

    initAutoSync() {
        window.addEventListener('online', () => {
            this.showToast('🌐 Conexión restaurada', 'success');
            this.syncUp().catch(console.error);
            this.wasOffline = false;
        });

        window.addEventListener('offline', () => {
            if (!this.wasOffline) {
                this.showToast('📵 Modo Offline activo', 'warning');
                this.wasOffline = true;
            }
        });

        setInterval(() => {
            if (navigator.onLine && !this.syncInProgress) {
                this.syncUp().catch(console.error);
            }
        }, 5 * 60 * 1000);
    }
}

export const syncService = new SyncService();