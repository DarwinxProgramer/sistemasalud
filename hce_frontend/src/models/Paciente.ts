export interface Paciente {
  /* ================= DATOS PRINCIPALES ================= */
  id: string;                 // UUID o igual a la cédula
  uuidOffline?: string;       // ID para sincronización (Primary Key)
  idPaciente?: number;        // ID generado por el backend (opcional offline)
  cedula: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  sexo: string;

  /* ================= HISTORIA CLÍNICA ================= */
  antecedentes?: any;         // Antecedentes globales del paciente
  historiaClinica?: any[];    // Consultas médicas

  /* ================= FLEXIBILIDAD ================= */
  // Contenedor para TODOS los campos del RegistroPaciente
  // (dirección, teléfono, vacunas, perinatales, etc.)
  [key: string]: any;
}
