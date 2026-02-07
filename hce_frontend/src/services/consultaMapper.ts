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
    id: number;
    medicamento: string;
    dosis: string;
    frecuencia: string;
    duracion: string;
    indicaciones: string;
}

export interface EstudioLaboratorioDTO {
    id: number;
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
