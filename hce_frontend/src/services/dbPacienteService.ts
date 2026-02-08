// src/services/dbPacienteService.ts
// Servicio de almacenamiento de pacientes usando IndexedDB (Dexie)

import { db, dbHelpers } from '../db/db';
export { dbHelpers };
import type { Paciente } from '../models/Paciente';
import { mapConsultaFrontendToBackend } from './consultaMapper';

/**
 * Obtener todos los pacientes desde IndexedDB
 */
export const obtenerPacientes = async (): Promise<Paciente[]> => {
    return await dbHelpers.getAllPacientes();
};

/**
 * Guardar el array completo de pacientes (bulk operation)
 */
export const guardarPacientes = async (pacientes: Paciente[]): Promise<void> => {
    await db.transaction('rw', db.pacientes, async () => {
        await db.pacientes.clear();
        await db.pacientes.bulkAdd(pacientes);
    });
};

/**
 * Guardar o actualizar un solo paciente
 */
export const guardarPaciente = async (paciente: Paciente): Promise<void> => {
    await dbHelpers.savePaciente(paciente);
};

/**
 * Registrar un paciente nuevo
 * Lanza error si ya existe
 */
export const registrarPaciente = async (paciente: Paciente): Promise<void> => {
    // 1. Verificamos si ya existe por cédula para no duplicar
    const existe = await dbHelpers.getPacienteByCedula(paciente.cedula);
    if (existe) {
        throw new Error('El paciente ya está registrado');
    }

    console.log('[DEBUG] Registrando paciente:', paciente.nombres);

    // --- MAPEO DE CATALOGOS (TEMPORAL) ---
    const mapaParroquias: Record<string, number> = {
        "Tarqui": 120, "El Vecino": 92, "Baños": 103, "Totoracocha": 100
    };

    const mapaGruposEtnicos: Record<string, number> = {
        "Mestizo": 15, "Blanco": 1, "Indígena": 2, "Afroecuatoriano": 3
    };

    // Preparar objeto para el Backend (TutorDTO, etc)
    const pacienteParaBackend: any = {
        cedula: paciente.cedula,
        primerNombre: paciente.nombres?.split(' ')[0] || '',
        segundoNombre: paciente.nombres?.split(' ')[1] || '',
        apellidoPaterno: paciente.apellidos?.split(' ')[0] || '',
        apellidoMaterno: paciente.apellidos?.split(' ')[1] || '',
        fechaNacimiento: paciente.fechaNacimiento,
        sexo: paciente.sexo,
        tipoSangre: paciente.tipoSangre,
        idGrupoEtnico: mapaGruposEtnicos[paciente.grupoEtnico] || 15, // Default Mestizo
        idParroquia: mapaParroquias[paciente.parroquia] || 92, // Default El Vecino
        uuidOffline: paciente.uuidOffline || paciente.id || self.crypto.randomUUID()
    };

    if (paciente.filiacion && paciente.filiacion.nombreResponsable) {
        const nombresCompletos = paciente.filiacion.nombreResponsable.split(' ');
        pacienteParaBackend.tutor = {
            primerNombre: nombresCompletos[0] || '',
            segundoNombre: nombresCompletos[1] || '',
            primerApellido: nombresCompletos[2] || '',
            segundoApellido: nombresCompletos[3] || '',
            parentesco: paciente.filiacion.parentesco,
            telefono: paciente.filiacion.telefonoContacto,
            nivelEducativo: paciente.filiacion.nivelEducativoResponsable,
            direccion: paciente.filiacion.domicilioActual,
            idParroquia: mapaParroquias[paciente.filiacion.parroquia] || 92
        };
    }

    // =========================================================================
    // LÓGICA DE GUARDADO (FIX CRÍTICO AQUÍ)
    // =========================================================================

    if (navigator.onLine) {
        // --- ONLINE ---
        console.log('[DEBUG] ONLINE: Enviando paciente al servidor...');
        const API_BASE_URL = (import.meta as any).env?.VITE_API_URL || 'http://localhost:8080/api';

        const response = await fetch(`${API_BASE_URL}/sync/up`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                entity: 'paciente',
                type: 'CREATE',
                data: pacienteParaBackend
            })
        });

        if (!response.ok) {
            throw new Error(`Error backend: ${response.statusText}`);
        }

        const mappings = await response.json();
        console.log('[DEBUG] ✅ Guardado en Backend. Mapeos:', mappings);

        // 1. Extraer el nuevo ID del servidor
        let nuevoIdServer = null;
        if (mappings && Array.isArray(mappings)) {
            const map = mappings.find((m: any) => m.uuidOffline === pacienteParaBackend.uuidOffline);
            if (map && map.newId) nuevoIdServer = map.newId;
        }

        // 2. ACTUALIZAR EL OBJETO LOCAL CON EL ID REAL
        if (nuevoIdServer) {
            paciente.id = nuevoIdServer;          // ID para Dexie (si es numérico)
            paciente.idPaciente = nuevoIdServer;  // ID para tu lógica de negocio
        }

        // 3. ¡IMPORTANTE! INICIALIZAR HISTORIA CLINICA VACÍA
        // Esto evita que falle la consulta que vas a agregar justo después
        paciente.historiaClinica = [];
        paciente.syncStatus = 'SYNCED';

        // 4. GUARDAR EN INDEXEDDB (FORCE PUT)
        // Usamos put para insertar o actualizar si ya existiera algo raro
        await db.pacientes.put(paciente);
        console.log(`[DEBUG] Paciente guardado localmente con ID: ${paciente.idPaciente}`);

    } else {
        // --- OFFLINE ---
        console.log('[DEBUG] OFFLINE: Guardando localmente...');

        // Aseguramos que tenga array
        paciente.historiaClinica = [];
        paciente.syncStatus = 'PENDING';

        // Guardamos paciente
        await dbHelpers.savePaciente(paciente);

        // Agregamos a cola
        await dbHelpers.addToSyncQueue({
            type: 'CREATE',
            entity: 'paciente',
            data: pacienteParaBackend
        });
    }
};

/**
 * Buscar un paciente por su número de cédula
 */
export const buscarPacientePorCedula = async (cedula: string): Promise<Paciente | undefined> => {
    return await dbHelpers.getPacienteByCedula(cedula);
};

/**
 * Agregar una consulta nueva al historial de un paciente
 */
export const agregarConsulta = async (cedula: string, consultaFrontend: any): Promise<boolean> => {
    // 1. Buscamos al paciente (Que ahora SÍ existirá gracias al fix de arriba)
    const paciente = await dbHelpers.getPacienteByCedula(cedula);

    if (!paciente) {
        console.error(`[DEBUG] CRÍTICO: Paciente con cédula ${cedula} no encontrado en local.`);
        return false;
    }

    // 2. Inicialización defensiva (Por si acaso)
    if (!paciente.historiaClinica) paciente.historiaClinica = [];

    // 3. Validar que el paciente tenga ID
    if (!paciente.idPaciente) {
        console.error('[ERROR] Paciente sin ID, no se puede guardar consulta');
        return false;
    }

    // 4. Preparar datos usando el mapper bidireccional
    const consultaParaBackend = mapConsultaFrontendToBackend(
        consultaFrontend,
        paciente.idPaciente
    );

    console.log('[DEBUG] Consulta mapeada para backend:', consultaParaBackend);

    try {
        // 5. Guardar en IndexedDB (Array local)
        paciente.historiaClinica.push(consultaFrontend);
        paciente.antecedentes = consultaFrontend.antecedentes; // Actualizar ficha

        await dbHelpers.savePaciente(paciente);
        console.log('[DEBUG] Consulta guardada en IndexedDB.');

        // 5. Cola de sincronización
        await dbHelpers.addToSyncQueue({
            type: 'CREATE',
            entity: 'consulta',
            data: consultaParaBackend
        });

        // 6. Intentar subir inmediatamente si hay internet
        if (navigator.onLine) {
            // Importación dinámica para evitar ciclos
            import('./syncService').then(m => m.syncService.syncUp().catch(err => console.error(err)));
        }

        return true;
    } catch (error) {
        console.error('[DEBUG] Error al guardar consulta:', error);
        return false;
    }
};

/**
 * Actualizar una consulta existente
 */
export const actualizarConsultaExistente = async (cedula: string, consultaEditada: any): Promise<boolean> => {
    const paciente = await dbHelpers.getPacienteByCedula(cedula);

    if (!paciente || !paciente.historiaClinica) return false;

    const consultaIndex = paciente.historiaClinica.findIndex((c: any) => c.id === consultaEditada.id);

    if (consultaIndex === -1) return false;

    // Reemplazar consulta
    paciente.historiaClinica[consultaIndex] = consultaEditada;
    paciente.antecedentes = consultaEditada.antecedentes;

    await dbHelpers.savePaciente(paciente);

    await dbHelpers.addToSyncQueue({
        type: 'UPDATE',
        entity: 'consulta',
        data: { cedula, consulta: consultaEditada }
    });

    return true;
};

/**
 * Actualizar datos del paciente (incluyendo antecedentes) y sincronizar
 */
export const actualizarPaciente = async (paciente: Paciente): Promise<boolean> => {
    try {
        // 1. Guardar localmente
        await dbHelpers.savePaciente(paciente);
        console.log('[DEBUG] Paciente actualizado localmente:', paciente.cedula);

        // 2. Preparar datos para backend (Mapeo inverso)
        // Nota: Reutilizamos lógica simplificada de registrarPaciente
        const mapaParroquias: Record<string, number> = { "Tarqui": 120, "El Vecino": 92, "Baños": 103, "Totoracocha": 100 };
        const mapaGruposEtnicos: Record<string, number> = { "Mestizo": 15, "Blanco": 1, "Indígena": 2, "Afroecuatoriano": 3 };

        const pacienteParaBackend: any = {
            idPaciente: paciente.idPaciente, // Importante para updates
            cedula: paciente.cedula,
            primerNombre: paciente.nombres?.split(' ')[0] || '',
            segundoNombre: paciente.nombres?.split(' ')[1] || '',
            apellidoPaterno: paciente.apellidos?.split(' ')[0] || '',
            apellidoMaterno: paciente.apellidos?.split(' ')[1] || '',
            fechaNacimiento: paciente.fechaNacimiento,
            sexo: paciente.sexo,
            tipoSangre: paciente.tipoSangre,
            idGrupoEtnico: mapaGruposEtnicos[paciente.grupoEtnico] || 15,
            idParroquia: mapaParroquias[paciente.parroquia] || 92,
            uuidOffline: paciente.uuidOffline,
            antecedentes: paciente.antecedentes // Enviamos antecedentes si el backend lo soporta
        };

        if (paciente.filiacion && paciente.filiacion.nombreResponsable) {
            const partes = paciente.filiacion.nombreResponsable.split(' ');
            pacienteParaBackend.tutor = {
                primerNombre: partes[0] || '',
                segundoNombre: partes[1] || '',
                primerApellido: partes[2] || '',
                segundoApellido: partes[3] || '',
                parentesco: paciente.filiacion.parentesco,
                telefono: paciente.filiacion.telefonoContacto,
                nivelEducativo: paciente.filiacion.nivelEducativoResponsable,
                direccion: paciente.filiacion.domicilioActual,
                idParroquia: mapaParroquias[paciente.filiacion.parroquia] || 92
            };
        }

        // 3. Cola de sincronización (PACIENTE)
        await dbHelpers.addToSyncQueue({
            type: 'UPDATE', // Siempre update porque ya existe localmente
            entity: 'paciente',
            data: pacienteParaBackend
        });

        // ---------------------------------------------------------
        // 4. SINCRONIZAR ANTECEDENTES PERINATALES (SI EXISTEN)
        // ---------------------------------------------------------
        if (paciente.antecedentes?.perinatales) {
            const ap = paciente.antecedentes.perinatales;

            // Mapear al DTO del backend (AntecedentePerinatalDTO)
            const perinatalDTO = {
                idPaciente: paciente.idPaciente,
                embarazoPlanificado: false, // Default
                controlesPrenatales: 0,     // Default
                antecedentes: `Gesta: ${ap.productoGestacion}, Semanas: ${ap.edadGestacional}, Parto: ${ap.viaParto}`,
                otrosAntecedentes: `Peso: ${ap.pesoNacimiento}, Talla: ${ap.tallaNacimiento}, APGAR: ${JSON.stringify(ap.apgar)}, Compl: ${JSON.stringify(ap.checksComplicaciones)}`,
                usuario: 'medico1',
                uuidOffline: crypto.randomUUID(), // Generar nuevo UUID para el antecedente
                origin: 'MOBILE'
            };

            console.log('[DEBUG] Encolando antecedente perinatal:', perinatalDTO);

            await dbHelpers.addToSyncQueue({
                type: 'CREATE',
                entity: 'antecedente-perinatal',
                data: perinatalDTO
            });
        }

        // 5. Intentar subir inmediatamente si hay internet
        if (navigator.onLine) {
            import('./syncService').then(m => m.syncService.syncUp().catch(err => console.error(err)));
        }

        return true;
    } catch (error) {
        console.error('[ERROR] Falló actualizarPaciente:', error);
        return false;
    }
};