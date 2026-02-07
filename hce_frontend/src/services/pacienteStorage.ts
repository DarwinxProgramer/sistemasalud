// src/services/pacienteStorage.ts
import type { Paciente } from "../models/Paciente"; // Agregada la palabra 'type'

const STORAGE_KEY = "hce_pacientes";

export const obtenerPacientes = (): Paciente[] => {
  const data = localStorage.getItem(STORAGE_KEY);
  return data ? JSON.parse(data) : [];
};

export const guardarPaciente = (pacientes: Paciente[]) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(pacientes));
};

/**
 * 2. Guarda el array completo de pacientes
 * Se exporta para permitir manipulaciones globales desde otros componentes
 */
export const guardarPacientes = (pacientes: Paciente[]) => {
  localStorage.setItem(STORAGE_KEY, JSON.stringify(pacientes));
};

/**
 * 3. Registrar un paciente nuevo
 */
export const registrarPaciente = (paciente: Paciente) => {
  const pacientes = obtenerPacientes();
  const existe = pacientes.some(p => p.cedula === paciente.cedula);
  if (existe) throw new Error("El paciente ya está registrado");
  pacientes.push(paciente);
  guardarPacientes(pacientes);
};

/**
 * 4. Buscar un paciente por su número de cédula
 */
export const buscarPacientePorCedula = (cedula: string): Paciente | undefined => {
  return obtenerPacientes().find(p => p.cedula === cedula);
};

/**
 * 5. AGREGAR UNA CONSULTA NUEVA
 * Incluye la actualización global de antecedentes en la ficha base
 */
export const agregarConsulta = (cedula: string, nuevaConsulta: any) => {
  const pacientes = obtenerPacientes();
  const index = pacientes.findIndex(p => p.cedula === cedula);

  if (index !== -1) {
    if (!pacientes[index].historiaClinica) {
      pacientes[index].historiaClinica = [];
    }
    
    // Añadimos la nueva consulta al historial
    pacientes[index].historiaClinica!.push(nuevaConsulta);
    
    // ACTUALIZACIÓN GLOBAL: Los antecedentes se sincronizan con la ficha base
    pacientes[index].antecedentes = nuevaConsulta.antecedentes;
    
    guardarPacientes(pacientes);
    return true;
  }
  return false;
};

/**
 * 6. ACTUALIZAR CONSULTA EXISTENTE
 * Refleja cambios editados tanto en la consulta como en la ficha base
 */
export const actualizarConsultaExistente = (cedula: string, consultaEditada: any) => {
  const pacientes = obtenerPacientes();
  const index = pacientes.findIndex(p => p.cedula === cedula);

  if (index !== -1) {
    const paciente = pacientes[index];
    if (paciente.historiaClinica) {
      const cIndex = paciente.historiaClinica.findIndex((c: any) => c.id === consultaEditada.id);
      
      if (cIndex !== -1) {
        // Reemplazamos la consulta antigua por la editada
        paciente.historiaClinica[cIndex] = consultaEditada;
        
        // ACTUALIZACIÓN GLOBAL: Mantenemos los antecedentes actualizados
        paciente.antecedentes = consultaEditada.antecedentes;
        
        guardarPacientes(pacientes);
        return true;
      }
    }
  }
  return false;
};
