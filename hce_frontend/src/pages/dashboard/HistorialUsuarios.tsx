import { useState, useEffect } from "react";
import { obtenerLogs } from "../../services/logStorage";

export default function HistorialUsuarios() {
  const [logs, setLogs] = useState<any[]>([]);
  const [nombreUsuario, setNombreUsuario] = useState('Cargando...');
  const [inicial, setInicial] = useState('U');

  useEffect(() => {
    // 1. CARGA DE DATOS DE LOGS
    const data = obtenerLogs();
    setLogs(Array.isArray(data) ? data : []);

    // 2. RECUPERAR USUARIO DEL LOGIN (LOCALSTORAGE)
    const storedUser = localStorage.getItem('usuarioLogueado');
    if (storedUser) {
      const userData = JSON.parse(storedUser);
      setNombreUsuario(userData.nombre);
      // Extrae la inicial del nombre del usuario
      const letra = userData.nombre.split(" ")[1]?.charAt(0).toUpperCase() || "U";
      setInicial(letra);
    }
  }, []);

  const formatearFecha = (fecha: any) => {
    if (!fecha) return "—";
    try {
      return new Date(fecha).toLocaleString();
    } catch {
      return "—";
    }
  };

  return (
    <div className="container-fluid p-0" style={{ backgroundColor: "#f4f7f6", minHeight: "100vh" }}>
      
      {/* --- ENCABEZADO SUPERIOR --- */}
      <div className="d-flex justify-content-between align-items-center px-4 py-3 bg-transparent">
        <div className="d-flex align-items-center">
          {/* Icono de historial a la izquierda */}
          <i className="bi bi-clock-history text-primary fs-4 me-3"></i>
          <h4 className="text-primary fw-normal m-0">Historial de Actividad</h4>
        </div>

        {/* Cuadro de perfil dinámico a la derecha */}
        <div className="d-flex align-items-center border rounded p-2 bg-white shadow-sm" style={{ minWidth: "220px" }}>
          <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-2" style={{ width: "40px", height: "40px", fontWeight: "bold" }}>
            {inicial}
          </div>
          <div>
            <div className="text-muted" style={{ fontSize: "10px", lineHeight: "1" }}>Profesional</div>
            <div className="text-dark" style={{ fontSize: "14px", fontWeight: "500" }}>{nombreUsuario}</div>
          </div>
        </div>
      </div>
      
      {/* Línea finita de separación */}
      <hr className="m-0 text-muted opacity-25" />

      {/* --- CONTENIDO DE LA TABLA CON CENTRADO DINÁMICO --- */}
      <div className="d-flex flex-column align-items-center justify-content-center" style={{ minHeight: "80vh", padding: "20px" }}>
        <div className="card shadow-sm border-0 w-100" style={{ maxWidth: "1100px", borderRadius: "15px" }}>
          <div className="table-responsive" style={{ borderRadius: "15px" }}>
            <table className="table table-hover align-middle mb-0">
              <thead className="table-light">
                <tr className="text-secondary small text-uppercase">
                  <th className="py-3 ps-4">Usuario</th>
                  <th className="py-3">Acción</th>
                  <th className="py-3">Paciente</th>
                  <th className="py-3 pe-4">Fecha / Hora</th>
                </tr>
              </thead>
              <tbody>
                {logs.length === 0 ? (
                  <tr>
                    <td colSpan={4} className="py-5 text-center text-muted">
                      <i className="bi bi-info-circle d-block mb-2 fs-3"></i>
                      No hay actividad registrada
                    </td>
                  </tr>
                ) : (
                  logs.map((log, i) => (
                    <tr key={i}>
                      <td className="ps-4 fw-bold text-dark">{log?.usuario || "Desconocido"}</td>
                      <td>
                        <span className="badge bg-light text-primary border px-2 py-1">
                          {log?.accion || "—"}
                        </span>
                      </td>
                      <td>{log?.paciente || "—"}</td>
                      <td className="pe-4 text-muted">{formatearFecha(log?.fecha)}</td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

    </div>
  );
}

