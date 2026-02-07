import React from 'react';
import { useNavigate } from 'react-router-dom';

interface Props {
  consultas: any[];
  idEdicionActual: string | null; // Corregido: sin espacio
  onVerExpediente: (consulta: any) => void;
  onEditarConsulta: (consulta: any) => void;
  onNuevaConsulta: () => void;
}

const ListaConsultasHistorial: React.FC<Props> = ({
  consultas,
  idEdicionActual,
  onVerExpediente,
  onEditarConsulta,
  onNuevaConsulta,
}) => {
  return (
    <div className="bg-white border-top shadow-sm p-4 d-flex flex-column flex-shrink-0 w-100" style={{ height: '280px' }}>
      <div className="d-flex justify-content-between align-items-center mb-2 w-100">
        <h6 className="text-success fw-bold m-0">
          <i className="bi bi-clock-history me-2"></i>HISTORIAL DE CONSULTAS
        </h6>
        <button className="btn btn-success fw-bold btn-sm" onClick={onNuevaConsulta}>
          + NUEVA CONSULTA
        </button>
      </div>

      <div className="flex-grow-1 overflow-auto w-100">
        <div className="row g-3 w-100 m-0">
          {consultas.slice().reverse().map((hist: any, i: number) => (
            <div key={hist.id || i} className="col-md-4 col-lg-3">
              <div className={`p-3 rounded border h-100 bg-white shadow-sm ${idEdicionActual === hist.id ? 'border-primary bg-primary bg-opacity-10' : ''}`}>
                <div className="d-flex justify-content-between align-items-center mb-2">
                  <span className="fw-bold small text-muted">{hist.fecha}</span>
                  <div className="d-flex gap-2">
                    {/* Botón Ojo: Ver Expediente */}
                    <button 
                      className="btn btn-sm btn-outline-info p-1 lh-1" 
                      title="Ver Expediente"
                      onClick={() => onVerExpediente(hist)}
                    >
                      <i className="bi bi-eye-fill"></i>
                    </button>
                    {/* Botón Lápiz: Editar */}
                    <button 
                      className="btn btn-sm btn-outline-warning p-1 lh-1" 
                      title="Editar Consulta"
                      onClick={() => onEditarConsulta(hist)}
                    >
                      <i className="bi bi-pencil-square"></i>
                    </button>
                  </div>
                </div>
                <strong className="d-block text-dark small text-truncate">
                  {hist.anamnesis?.motivoConsulta || "Sin motivo"}
                </strong>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default ListaConsultasHistorial;