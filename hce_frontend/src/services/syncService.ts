// src/services/syncService.ts
// Servicio de sincronización bidireccional con el backend - CON NOTIFICACIONES TOAST

import { db, dbHelpers } from '../db/db';

const API_BASE_URL = import.meta.env.VITE_API_URL || '/api';

export interface SyncStatus {
    lastSync: number | null;
    syncing: boolean;
    pendingChanges: number;
    online: boolean;
}

// Tipo para callbacks de toast (inyección de dependencia)
type ToastCallback = (message: string, type?: 'info' | 'success' | 'warning' | 'error') => void;

class SyncService {
    private syncInProgress = false;
    private listeners: Array<(status: SyncStatus) => void> = [];
    private toastCallback: ToastCallback | null = null;
    private wasOffline = false;

    /**
     * Configurar callback para mostrar toasts
     * (Se llama desde App.tsx después de inicializar ToastContext)
     */
    setToastCallback(callback: ToastCallback) {
        this.toastCallback = callback;
    }

    private showToast(message: string, type?: 'info' | 'success' | 'warning' | 'error') {
        if (this.toastCallback) {
            this.toastCallback(message, type);
        }
    }

    /**
     * Obtener estado actual de sincronización
     */
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

    /**
     * Suscribirse a cambios de estado de sincronización
     */
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

            this.showToast('📥 Descargando datos del servidor...', 'info');
            console.log('[SyncService] Iniciando sync down desde', `${API_BASE_URL}/sync/down`);

            const response = await fetch(`${API_BASE_URL}/sync/down`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }

            const data = await response.json();
            console.log('[SyncService] Datos recibidos:', data);

            // Guardar pacientes en IndexedDB
            if (data.pacientes && Array.isArray(data.pacientes)) {
                const pacientesToSave = data.pacientes.map((p: any) => ({
                    ...p,
                    // Asegurar que exista uuidOffline, si no, usar uno temporal o generado
                    uuidOffline: p.uuidOffline || p.uuid_offline || self.crypto.randomUUID()
                }));

                console.log('[SyncService] Guardando pacientes:', pacientesToSave.length, pacientesToSave[0]);

                const pacientesConUuid = data.pacientes.map((p: any) => {
                    // Mapeo de nombres y apellidos (Backend -> Frontend)
                    const nombres = [p.primerNombre, p.segundoNombre].filter(Boolean).join(' ').trim();
                    const apellidos = [p.apellidoPaterno, p.apellidoMaterno].filter(Boolean).join(' ').trim();

                    // Mapeo de filiación (Tutor -> Filiacion)
                    let filiacion = null;
                    if (p.tutor) {
                        filiacion = {
                            nombreResponsable: [
                                p.tutor.primerNombre,
                                p.tutor.segundoNombre,
                                p.tutor.primerApellido,
                                p.tutor.segundoApellido
                            ].filter(Boolean).join(' ').trim(),
                            parentesco: p.tutor.parentesco,
                            telefonoContacto: p.tutor.telefono,
                            nivelEducativoResponsable: p.tutor.nivelEducativo,
                            domicilioActual: p.tutor.direccion,
                            parroquia: p.tutor.idParroquia // Mapear ID si es necesario o dejar como está
                        };
                    }

                    return {
                        ...p,
                        nombres: nombres,     // Propiedad requerida por la UI
                        apellidos: apellidos, // Propiedad requerida por la UI
                        filiacion: filiacion, // Propiedad requerida por el modelo Paciente
                        uuidOffline: p.uuidOffline || p.id || self.crypto.randomUUID()
                    };
                });

                await db.transaction('rw', db.pacientes, async () => {
                    await db.pacientes.clear();
                    await db.pacientes.bulkPut(pacientesConUuid);
                });


                // Verificar si no hay datos
                if (pacientesConUuid.length === 0) {
                    this.showToast?.('ℹ️ No hay pacientes registrados aún en el sistema', 'info');
                    console.log('✅ Sync DOWN: Base de datos vacía - esperando primer registro');
                } else {
                    this.showToast?.(`✅ Sincronización completada: ${pacientesConUuid.length} paciente(s) descargado(s)`, 'success');
                    console.log(`✅ Sync DOWN: ${pacientesConUuid.length} pacientes descargados`);
                }

            }

            // Guardar catálogos (provincias, cantones, etc.) -> COMENTADO TEMPORALMENTE POR REFACTORIZACION BACKEND
            /* if (data.antecedentesFamiliares && Array.isArray(data.antecedentesFamiliares)) {
                await db.transaction('rw', db.catalogos, async () => {
                    await db.catalogos.bulkAdd(
                        data.antecedentesFamiliares.map((item: any) => ({
                            tipo: 'antecedente_familiar',
                            ...item
                        }))
                    );
                });
                console.log(`[SyncService] Sincronizados catálogos`);
            } */

            // ⭐ NUEVO: Procesar consultas del backend
            if (data.consultas && Array.isArray(data.consultas)) {
                console.log(`[SyncService] Procesando ${data.consultas.length} consultas`);

                // Importar mapper dinámicamente
                const { mapConsultaBackendToFrontend } = await import('./consultaMapper');

                // Mapear consultas a pacientes
                for (const consultaBackend of data.consultas) {
                    try {
                        // Buscar paciente por idPaciente
                        const paciente = await db.pacientes
                            .where('idPaciente')
                            .equals(consultaBackend.idPaciente)
                            .first();

                        if (paciente) {
                            // Inicializar historiaClinica si no existe
                            if (!paciente.historiaClinica) {
                                paciente.historiaClinica = [];
                            }

                            // Convertir a formato frontend y agregar
                            const consultaFrontend = mapConsultaBackendToFrontend(consultaBackend);

                            // Evitar duplicados (por id de consulta)
                            const existe = paciente.historiaClinica.some(
                                (c: any) => c.id === consultaFrontend.id
                            );

                            if (!existe) {
                                paciente.historiaClinica.push(consultaFrontend);
                                await db.pacientes.put(paciente);
                            }
                        } else {
                            console.warn(`[SyncService] Paciente ${consultaBackend.idPaciente} no encontrado para consulta ${consultaBackend.idConsulta}`);
                        }
                    } catch (error) {
                        console.error(`[SyncService] Error procesando consulta ${consultaBackend.idConsulta}:`, error);
                    }
                }

                console.log(`[SyncService] ✅ ${data.consultas.length} consultas sincronizadas`);
            }

            // Guardar timestamp de última sincronización
            await dbHelpers.setMetadata('lastSyncTimestamp', Date.now());

        } catch (error) {
            console.error('[SyncService] Error en sync down:', JSON.stringify(error, Object.getOwnPropertyNames(error)));
            this.showToast('❌ Error al descargar datos', 'error');
            throw error;
        } finally {
            this.syncInProgress = false;
            await this.notifyListeners();
        }
    }

    /**
     * Sincronización de subida: Enviar cambios pendientes al servidor
     */
    async syncUp(): Promise<void> {
        if (!navigator.onLine) {
            console.warn('[SyncService] Sin conexión, skip sync up');
            return;
        }

        const pendingItems = await dbHelpers.getPendingSyncItems();

        if (pendingItems.length === 0) {
            console.log('[SyncService] No hay cambios pendientes para sincronizar');
            return;
        }

        try {
            this.syncInProgress = true;
            await this.notifyListeners();

            const totalItems = pendingItems.length;
            this.showToast(`🔄 Sincronizando ${totalItems} paciente(s) pendiente(s)...`, 'info');
            console.log(`[SyncService] Sincronizando ${totalItems} cambios pendientes`);

            let syncedCount = 0;

            for (const item of pendingItems) {
                try {
                    // TODO: Implementar endpoint /api/sync/up en el backend
                    const response = await fetch(`${API_BASE_URL}/sync/up`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(item)
                    });

                    if (response.ok) {
                        // Procesar respuesta con mapeos de IDs
                        const mappings = await response.json();

                        if (mappings && Array.isArray(mappings)) {
                            for (const map of mappings) {
                                if (map.uuidOffline && map.newId && map.entityType === 'paciente') {
                                    console.log(`[SyncService] Actualizando ID local para ${map.uuidOffline} -> ${map.newId}`);
                                    // Actualizar ID en la base de datos local
                                    await db.pacientes.update(map.uuidOffline, { idPaciente: map.newId });
                                }
                            }
                        }

                        await dbHelpers.markAsSynced(item.id!);
                        syncedCount++;

                        // Mostrar progreso con conteo regresivo
                        const remaining = totalItems - syncedCount;
                        if (remaining > 0) {
                            this.showToast(`⏳ ${remaining} paciente(s) restante(s)...`, 'info');
                        }

                        console.log(`[SyncService] Sincronizado item ${item.id} (${syncedCount}/${totalItems})`);
                    } else {
                        console.error(`[SyncService] Error sincronizando item ${item.id}:`, response.statusText);
                    }
                } catch (error) {
                    console.error(`[SyncService] Error enviando item ${item.id}:`, error);
                    // Incrementar contador de reintentos
                    await db.syncQueue.update(item.id!, { retries: item.retries + 1 });
                }
            }

            // Limpiar items sincronizados
            await dbHelpers.clearSyncedItems();

            if (syncedCount > 0) {
                this.showToast(`✅ ${syncedCount} paciente(s) sincronizado(s) exitosamente`, 'success');
            }

        } catch (error) {
            console.error('[SyncService] Error en sync up:', error);
            this.showToast('❌ Error al sincronizar cambios', 'error');
            throw error;
        } finally {
            this.syncInProgress = false;
            await this.notifyListeners();
        }
    }

    /**
     * Sincronización completa (bajada + subida)
     */
    async sync(): Promise<void> {
        if (this.syncInProgress) {
            console.warn('[SyncService] Sincronización ya en progreso');
            return;
        }

        console.log('[SyncService] Iniciando sincronización completa');

        try {
            // 1. PRIMERO: Subir cambios locales (Evita que clear() borre datos nuevos)
            await this.syncUp();

            // 2. SEGUNDO: Descargar datos del servidor
            await this.syncDown();

            console.log('[SyncService] Sincronización completa exitosa');
        } catch (error) {
            console.error('[SyncService] Error en sincronización:', error);
            throw error;
        }
    }

    /**
     * Inicializar sincronización automática
     */
    initAutoSync() {
        // Sincronización al detectar conexión - SOLO SUBIR CAMBIOS PENDIENTES
        window.addEventListener('online', async () => {
            console.log('[SyncService] Conexión restaurada');

            // Obtener cambios pendientes para mostrar en notificación
            const pending = await dbHelpers.getPendingSyncItems();
            const pendingCount = pending.length;

            if (pendingCount > 0) {
                this.showToast(`🌐 Sincronizando ${pendingCount} paciente(s)...`, 'info');
                // SOLO sincronizar hacia arriba (subir cambios pendientes)
                this.syncUp().catch(console.error);
            } else {
                this.showToast('🌐 Conexión restablecida', 'success');
            }

            this.wasOffline = false;
        });

        // Notificar pérdida de conexión
        window.addEventListener('offline', async () => {
            console.log('[SyncService] Conexión perdida');

            if (!this.wasOffline) {
                this.showToast('📵 Sin conexión - Sus cambios se guardarán para sincronizar después', 'warning');
                this.wasOffline = true;
            }
        });

        // Sincronización periódica cada 5 minutos (si hay conexión)
        // SOLO subir cambios, NO descargar
        setInterval(() => {
            if (navigator.onLine && !this.syncInProgress) {
                console.log('[SyncService] Auto-sync periódico - solo subida');
                this.syncUp().catch(console.error);
            }
        }, 5 * 60 * 1000);
    }
}

// Instancia singleton
export const syncService = new SyncService();
