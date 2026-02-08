import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { obtenerPacientes } from '../../services/dbPacienteService';

export default function HistorialIndex() {
    const navigate = useNavigate();
    const [pacientes, setPacientes] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);
    const [busqueda, setBusqueda] = useState("");

    useEffect(() => {
        const cargarDatos = async () => {
            setLoading(true);
            try {
                const lista = await obtenerPacientes();
                setPacientes(Array.isArray(lista) ? lista : []);
            } catch (error) {
                console.error("Error cargando pacientes:", error);
            } finally {
                setLoading(false);
            }
        };
        cargarDatos();
    }, []);

    const filtrados = pacientes.filter(p =>
        `${p.nombres} ${p.apellidos} ${p.cedula}`.toLowerCase().includes(busqueda.toLowerCase())
    );

    if (loading) {
        return (
            <div className="d-flex justify-content-center align-items-center vh-100 bg-light">
                <div className="spinner-border text-primary" role="status"></div>
            </div>
        );
    }

    return (
        <div className="container-fluid p-5 bg-light min-vh-100">
            <h3 className="fw-bold text-primary mb-4"><i className="bi bi-folder2-open me-2"></i>Historiales Clínicos</h3>

            <div className="bg-white p-4 rounded shadow-sm mb-4">
                <input
                    type="text"
                    className="form-control form-control-lg"
                    placeholder="Buscar paciente por nombre o cédula..."
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)}
                />
            </div>

            <div className="row g-3">
                {filtrados.length > 0 ? filtrados.map(p => (
                    <div key={p.cedula} className="col-md-6 col-lg-4">
                        <div className="card shadow-sm border-0 h-100 hover-shadow" style={{ transition: '0.3s' }}>
                            <div className="card-body d-flex align-items-center">
                                <div className="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3 flex-shrink-0" style={{ width: '50px', height: '50px', fontSize: '1.2rem' }}>
                                    {(p.nombres || '?').charAt(0)}{(p.apellidos || '?').charAt(0)}
                                </div>
                                <div className="flex-grow-1">
                                    <h6 className="fw-bold mb-0 text-dark">{p.apellidos || 'Sin apellido'} {p.nombres || 'Sin nombre'}</h6>
                                    <small className="text-muted d-block">C.I: {p.cedula}</small>
                                    <small className="text-success fw-bold" style={{ fontSize: '0.8rem' }}>
                                        {p.historiaClinica?.length || 0} Consultas registradas
                                    </small>
                                </div>
                                <button className="btn btn-outline-primary btn-sm ms-2" onClick={() => navigate(`/reporte-hce/${p.cedula}`)}>
                                    <i className="bi bi-eye-fill"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                )) : (
                    <div className="col-12 text-center py-5 text-muted">
                        <i className="bi bi-search display-4 mb-3 d-block opacity-50"></i>
                        No se encontraron pacientes.
                    </div>
                )}
            </div>
        </div>
    );
}
