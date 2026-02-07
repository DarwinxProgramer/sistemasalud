export const registrarLog = (log: { usuario: string, accion: string, paciente: string, fecha: string }) => {
  const logs = JSON.parse(localStorage.getItem("logs") || "[]");
  logs.push(log);
  localStorage.setItem("logs", JSON.stringify(logs));
};

export const obtenerLogs = () => {
  return JSON.parse(localStorage.getItem("logs") || "[]");
};
