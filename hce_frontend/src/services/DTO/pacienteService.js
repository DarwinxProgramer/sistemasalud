import api from "./api";

export const getPacientes = async () => {
    const response = await api.get("/pacientes");
    return response.data;
};

export const registrarPaciente = async (paciente) => {
    const response = await api.post("/pacientes", paciente);
    return response.data;
};

export const actualizarPaciente = async (id, paciente) => {
    const response = await api.put(`/pacientes/${id}`, paciente);
    return response.data;
};

export const eliminarPaciente = async (id) => {
    const response = await api.delete(`/pacientes/${id}`);
    return response.data;
};

export const buscarPacientePorCedula = async (cedula) => {
    const response = await api.get(`/pacientes/cedula/${cedula}`);
    return response.data;
};

export const buscarPacientePorId = async (id) => {
    const response = await api.get(`/pacientes/${id}`);
    return response.data;
};

export const obtenerPacientes = async () => {
    const response = await api.get("/pacientes");
    return response.data;
};

export const obtenerPacientesActivos = async () => {
    const response = await api.get("/pacientes/activos");
    return response.data;
};

export const obtenerPacientesInactivos = async () => {
    const response = await api.get("/pacientes/inactivos");
    return response.data;
};

export const obtenerPacientesPorProvincia = async (provincia) => {
    const response = await api.get(`/pacientes/provincia/${provincia}`);
    return response.data;
};

export const obtenerPacientesPorCanton = async (canton) => {
    const response = await api.get(`/pacientes/canton/${canton}`);
    return response.data;
};

export const obtenerPacientesPorParroquia = async (parroquia) => {
    const response = await api.get(`/pacientes/parroquia/${parroquia}`);
    return response.data;
};