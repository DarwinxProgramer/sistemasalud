// src/services/consultaMapper.ts
// Mapper para convertir ConsultaDTO del backend al formato del frontend

export interface ConsultaBackend {
    idConsulta: number;
    idPaciente: number;
    fecha: string;
    hora: string;
    motivo: string;
    enfermedadActual: string;
    peso: number;
    talla: number;
    temperatura: number;
    fc: number;
    fr: number;
    spo2: number;
    diagnosticoTexto: string;
    tipoDiagnostico: string;
    usuario: string;
    listaPlan?: PlanTerapeuticoDTO[];
    listaEstudios?: EstudioLaboratorioDTO[];
}

export interface PlanTerapeuticoDTO {
    id?: number;
    medicamento: string;
    dosis: string;
    frecuencia: string;
    duracion: string;
    indicaciones: string;
}

export interface EstudioLaboratorioDTO {
    id?: number;
    tipo: string;
    descripcion: string;
    fecha: string;
    resultado: string;
}

/**
 * Convierte ConsultaDTO del backend al formato que usa el frontend
 * en paciente.historiaClinica[]
 */
export function mapConsultaBackendToFrontend(consulta: ConsultaBackend): any {
    return {
        // Identificador único (usar idConsulta del backend)
        id: `consulta-${consulta.idConsulta}`,

        // Fecha y hora
        fecha: consulta.fecha,
        hora: consulta.hora,

        // Motivo y enfermedad actual
        motivoConsulta: consulta.motivo,
        enfermedadActual: consulta.enfermedadActual,

        // Signos vitales
        signosVitales: {
            peso: consulta.peso,
            talla: consulta.talla,
            temperatura: consulta.temperatura,
            fc: consulta.fc,
            fr: consulta.fr,
            spo2: consulta.spo2
        },

        // Diagnóstico
        diagnostico: {
            texto: consulta.diagnosticoTexto,
            tipo: consulta.tipoDiagnostico
        },

        // Plan terapéutico
        planTerapeutico: consulta.listaPlan?.map(plan => ({
            medicamento: plan.medicamento,
            dosis: plan.dosis,
            frecuencia: plan.frecuencia,
            duracion: plan.duracion,
            indicaciones: plan.indicaciones
        })) || [],

        // Estudios de laboratorio
        estudios: consulta.listaEstudios?.map(estudio => ({
            tipo: estudio.tipo,
            descripcion: estudio.descripcion,
            fecha: estudio.fecha,
            resultado: estudio.resultado
        })) || [],

        // Metadata
        usuario: consulta.usuario,
        sincronizado: true  // Viene del servidor
    };
}

/**
 * Convierte consulta del frontend al formato DTO del backend
 */
export function mapConsultaFrontendToBackend(consultaFrontend: any, idPaciente: number): any {
    return {
        idPaciente: idPaciente,
        fecha: consultaFrontend.fecha || new Date().toISOString().split('T')[0],
        hora: consultaFrontend.hora || new Date().toLocaleTimeString('en-GB', { hour: '2-digit', minute: '2-digit', second: '2-digit' }),
        motivo: consultaFrontend.motivo || consultaFrontend.motivoConsulta || '',
        enfermedadActual: consultaFrontend.enfermedadActual || '',

        // Signos vitales - intentar de examenFisico.vitales o signosVitales
        peso: parseFloat(consultaFrontend.examenFisico?.vitales?.peso || consultaFrontend.signosVitales?.peso || 0),
        talla: parseFloat(consultaFrontend.examenFisico?.vitales?.talla || consultaFrontend.signosVitales?.talla || 0),
        temperatura: parseFloat(consultaFrontend.examenFisico?.vitales?.temperatura || consultaFrontend.signosVitales?.temperatura || 0),
        fc: parseInt(consultaFrontend.examenFisico?.vitales?.fc || consultaFrontend.signosVitales?.fc || 0),
        fr: parseInt(consultaFrontend.examenFisico?.vitales?.fr || consultaFrontend.signosVitales?.fr || 0),
        spo2: parseInt(consultaFrontend.examenFisico?.vitales?.spo2 || consultaFrontend.signosVitales?.spo2 || 0),

        // Diagnóstico
        diagnosticoTexto: consultaFrontend.diagnostico?.principal?.descripcion || consultaFrontend.diagnosticoTexto || '',
        tipoDiagnostico: consultaFrontend.diagnostico?.principal?.tipo || consultaFrontend.tipoDiagnostico || 'Presuntivo',

        // UUID y sync
        uuidOffline: consultaFrontend.id,
        syncStatus: 'PENDING',
        origin: 'MOBILE',

        usuario: consultaFrontend.usuario || localStorage.getItem('usuario') || 'medico1',

        // Arrays estructurados para planes y estudios
        listaPlan: mapPlanFrontendToBackend(consultaFrontend),
        listaEstudios: mapEstudiosFrontendToBackend(consultaFrontend)
    };
}

/**
 * Extraer y mapear planes terapéuticos del formato frontend
 */
function mapPlanFrontendToBackend(consultaFrontend: any): PlanTerapeuticoDTO[] {
    const planes: PlanTerapeuticoDTO[] = [];

    // Si vienen del formato nuevo (array de planes)
    if (consultaFrontend.planTerapeutico && Array.isArray(consultaFrontend.planTerapeutico)) {
        return consultaFrontend.planTerapeutico.map((plan: any) => ({
            medicamento: plan.medicamento || '',
            dosis: plan.dosis || '',
            frecuencia: plan.frecuencia || '',
            duracion: plan.duracion || '',
            indicaciones: plan.indicaciones || ''
        }));
    }

    // Si vienen del formato antiguo (diagnostico.plan)
    if (consultaFrontend.diagnostico?.plan) {
        const plan = consultaFrontend.diagnostico.plan;

        // Farmacológico
        if (plan.farmacologico) {
            const farma = plan.farmacologico;
            if (farma.esquema || farma.viaVenosa || farma.viaOral) {
                planes.push({
                    medicamento: [farma.esquema, farma.viaVenosa, farma.viaOral]
                        .filter(Boolean).join('; ') || 'No especificado',
                    dosis: '',
                    frecuencia: '',
                    duracion: '',
                    indicaciones: 'Farmacológico'
                });
            }
        }

        // No farmacológico
        if (plan.noFarmacologico) {
            const noFarma = plan.noFarmacologico;
            const indicaciones = [];
            if (noFarma.hidratacion) indicaciones.push('Hidratación');
            if (noFarma.dieta) indicaciones.push('Dieta');
            if (noFarma.oxigeno) indicaciones.push('Oxígeno');
            if (noFarma.fisio) indicaciones.push('Fisioterapia');
            if (noFarma.otros) indicaciones.push(noFarma.otros);

            if (indicaciones.length > 0) {
                planes.push({
                    medicamento: 'Tratamiento no farmacológico',
                    dosis: '',
                    frecuencia: '',
                    duracion: '',
                    indicaciones: indicaciones.join(', ')
                });
            }
        }
    }

    return planes;
}

/**
 * Extraer y mapear estudios de laboratorio del formato frontend
 */
function mapEstudiosFrontendToBackend(consultaFrontend: any): EstudioLaboratorioDTO[] {
    const estudios: EstudioLaboratorioDTO[] = [];

    // Si vienen del formato nuevo (array)
    if (consultaFrontend.estudios && Array.isArray(consultaFrontend.estudios)) {
        return consultaFrontend.estudios.map((est: any) => ({
            tipo: est.tipo || '',
            descripcion: est.descripcion || '',
            fecha: est.fecha || new Date().toISOString().split('T')[0],
            resultado: est.resultado || ''
        }));
    }

    // Si vienen del formato antiguo (string de estudios solicitados)
    if (consultaFrontend.diagnostico?.estudios) {
        const estudiosSolicitados = consultaFrontend.diagnostico.estudios;
        if (typeof estudiosSolicitados === 'string' && estudiosSolicitados.trim()) {
            // Dividir por comas y crear un estudio por cada uno
            const lista = estudiosSolicitados.split(',').map((s: string) => s.trim()).filter(Boolean);
            lista.forEach((nombreEstudio: string) => {
                estudios.push({
                    tipo: nombreEstudio,
                    descripcion: 'Solicitado',
                    fecha: new Date().toISOString().split('T')[0],
                    resultado: ''
                });
            });
        }
    }

    // Agregar resultados si existen
    if (consultaFrontend.diagnostico?.resultados && Array.isArray(consultaFrontend.diagnostico.resultados)) {
        consultaFrontend.diagnostico.resultados.forEach((res: any) => {
            estudios.push({
                tipo: res.tipo || 'Resultado',
                descripcion: res.descripcion || '',
                fecha: res.fecha || new Date().toISOString().split('T')[0],
                resultado: res.valor || res.resultado || ''
            });
        });
    }

    return estudios;
}
