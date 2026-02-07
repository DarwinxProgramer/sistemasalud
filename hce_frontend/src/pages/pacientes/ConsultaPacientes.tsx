import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { obtenerPacientes } from "../../services/dbPacienteService";

export default function ConsultaPacientes() {
  const navigate = useNavigate();
  const [pacientes, setPacientes] = useState<any[]>([]);
  const [busqueda, setBusqueda] = useState("");
  const [pacienteConsultas, setPacienteConsultas] = useState<any | null>(null);

  useEffect(() => {
    const cargarPacientes = async () => {
      try {
        const lista = await obtenerPacientes();
        setPacientes(Array.isArray(lista) ? lista : []);
      } catch (error) {
        console.error("Error cargando pacientes:", error);
      }
    };
    cargarPacientes();
  }, []);

  const pacientesFiltrados = pacientes.filter(p =>
    `${p.nombres} ${p.apellidos} ${p.cedula}`.toLowerCase().includes(busqueda.toLowerCase())
  );

  return (
    <div className="container-fluid p-4 bg-light min-vh-100">
      <div className="d-flex justify-content-between align-items-center mb-4 bg-white p-3 rounded shadow-sm">
        <h4 className="text-primary fw-bold m-0">Panel de Control de Pacientes</h4>
        <input
          type="text" className="form-control shadow-sm" style={{ maxWidth: "320px" }}
          placeholder="Buscar paciente..." value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      <div className="card shadow-sm border-0 overflow-hidden">
        <div className="table-responsive">
          <table className="table table-hover align-middle mb-0" style={{ minWidth: "1000px" }}>
            <thead className="table-primary text-uppercase small">
              <tr>
                <th>Cédula</th>
                <th>Paciente</th>
                <th>F. Nacimiento</th>
                <th className="text-center">Expedientes</th>
              </tr>
            </thead>
            <tbody>
              {pacientesFiltrados.map((p) => (
                <tr key={p.cedula}>
                  <td className="fw-bold">{p.cedula}</td>
                  <td>{p.apellidos} {p.nombres}</td>
                  <td>{p.fechaNacimiento}</td>
                  <td className="text-center">
                    <div className="d-flex justify-content-center gap-2">
                      <button className="btn btn-primary btn-sm" onClick={() => navigate(`/historial/${p.cedula}`)}>
                        <i className="bi bi-plus-lg me-1"></i> Nueva
                      </button>

                      {/* ✅ BOTÓN AZUL ACTUALIZADO PARA VISTA PREVIA SOLO LECTURA */}
                      <button className="btn btn-outline-primary btn-sm" onClick={() => navigate(`/reporte-hce/${p.cedula}`)}>
                        <i className="bi bi-person-vcard me-1"></i> Vista Previa HCE
                      </button>

                      <button className="btn btn-outline-success btn-sm" onClick={() => setPacienteConsultas(p)}>
                        <i className="bi bi-clock-history me-1"></i> Consultas
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* MODAL VERDE: LISTADO DE CONSULTAS (Mantiene su funcionalidad de edición) */}
      {pacienteConsultas && (
        <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.6)', zIndex: 1050 }}>
          <div className="modal-dialog modal-lg modal-dialog-centered">
            <div className="modal-content border-0 shadow-lg">
              <div className="modal-header bg-success text-white">
                <h6 className="modal-title fw-bold">CONSULTAS: {pacienteConsultas.nombres}</h6>
                <button type="button" className="btn-close btn-close-white" onClick={() => setPacienteConsultas(null)}></button>
              </div>
              <div className="modal-body bg-light" style={{ maxHeight: '70vh', overflowY: 'auto' }}>
                {pacienteConsultas.historiaClinica?.length > 0 ? (
                  pacienteConsultas.historiaClinica.slice().reverse().map((c: any, i: number) => (
                    <div key={i} className="card mb-3 border-0 shadow-sm">
                      <div className="card-header bg-white d-flex justify-content-between align-items-center py-2">
                        <span className="fw-bold text-success">{c.fecha} <small className="text-muted">{c.hora}</small></span>
                        <button className="btn btn-sm btn-warning fw-bold" onClick={() => navigate(`/historial/${pacienteConsultas.cedula}`, { state: { consultaAEditar: c } })}>
                          <i className="bi bi-pencil-square me-1"></i> Editar
                        </button>
                      </div>
                      <div className="card-body py-2">
                        <p className="mb-1 small"><strong>Motivo:</strong> {c.motivo}</p>
                        <p className="mb-0 small text-muted"><strong>Diagnóstico:</strong> {c.diagnostico?.principal?.descripcion || "—"}</p>
                      </div>
                    </div>
                  ))
                ) : <div className="p-4 text-center">No hay consultas registradas.</div>}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
