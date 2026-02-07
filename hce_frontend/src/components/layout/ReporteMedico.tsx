import React from 'react';

interface ReporteProps {
  paciente: any;
  consulta: any;
  nombreDoctor: string;
  onClose: () => void;
}

export default function ReporteMedico({ paciente, consulta, nombreDoctor, onClose }: ReporteProps) {
  
  // Función para imprimir
  const handlePrint = () => {
    window.print();
  };

  if (!paciente || !consulta) return null;

  // Filtramos el historial para que no se rompa si es undefined
  const historialPrevio = paciente.historiaClinica?.filter((c: any) => c.id !== consulta.id) || [];

  return (
    <div className="modal d-block" style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1050 }}>
      <div className="modal-dialog modal-xl modal-dialog-scrollable">
        <div className="modal-content">
          
          {/* HEADER (Botones, no sale en impresión) */}
          <div className="modal-header d-print-none bg-primary text-white">
            <h5 className="modal-title"><i className="bi bi-file-earmark-medical me-2"></i>Vista Previa de Reporte</h5>
            <div>
                <button className="btn btn-light btn-sm me-2 fw-bold" onClick={handlePrint}>
                    <i className="bi bi-printer me-2"></i> Imprimir / Guardar PDF
                </button>
                <button className="btn btn-outline-light btn-sm" onClick={onClose}>Cerrar</button>
            </div>
          </div>

          {/* CUERPO DEL REPORTE (Diseño A4) */}
          <div className="modal-body p-0 bg-secondary bg-opacity-25">
            <div className="bg-white p-5 mx-auto shadow-sm my-4" style={{ maxWidth: '210mm', minHeight: '297mm' }}>
                
                {/* 1. ENCABEZADO */}
                <div className="text-center border-bottom border-2 border-dark pb-3 mb-4">
                    <h3 className="fw-bold text-dark m-0">HISTORIA CLÍNICA PEDIÁTRICA</h3>
                    <p className="text-muted small m-0">Reporte Integral de Atención Médica</p>
                    <small className="text-secondary">Fecha de Emisión: {new Date().toLocaleDateString()} {new Date().toLocaleTimeString()}</small>
                </div>

                {/* 2. DATOS DEL PACIENTE */}
                <div className="mb-4 border border-secondary rounded p-3">
                    <h6 className="fw-bold text-uppercase border-bottom pb-1 mb-2 bg-light px-2">1. Datos del Paciente</h6>
                    <div className="row g-2 small text-dark">
                        <div className="col-8"><strong>Nombres:</strong> {paciente.nombres} {paciente.apellidos}</div>
                        <div className="col-4"><strong>Cédula:</strong> {paciente.cedula}</div>
                        
                        <div className="col-4"><strong>F. Nacimiento:</strong> {paciente.fechaNacimiento}</div>
                        <div className="col-4"><strong>Edad:</strong> {paciente.edad}</div>
                        <div className="col-4"><strong>Sexo:</strong> {paciente.sexo}</div>

                        <div className="col-6"><strong>Lugar:</strong> {paciente.provincia}, {paciente.canton}</div>
                        <div className="col-6"><strong>Dirección:</strong> {paciente.filiacion?.domicilioActual || '-'}</div>

                        <div className="col-12 mt-1 pt-1 border-top border-secondary border-opacity-25">
                            <strong>Representante:</strong> {paciente.filiacion?.nombreResponsable} 
                            <span className="ms-3"><strong>Parentesco:</strong> {paciente.filiacion?.parentesco}</span>
                            <span className="ms-3"><strong>Teléfono:</strong> {paciente.filiacion?.telefonoContacto}</span>
                        </div>
                    </div>
                </div>

                {/* 3. DETALLE CONSULTA */}
                <div className="mb-4">
                    <h6 className="fw-bold text-uppercase border-bottom pb-1 mb-2 bg-light px-2">2. Consulta Actual ({consulta.fecha})</h6>
                    
                    <div className="ps-2 mb-3">
                        <p className="mb-1 small"><strong>Motivo:</strong> {consulta.motivo}</p>
                        <p className="mb-0 small"><strong>Enfermedad Actual:</strong> {consulta.enfermedadActual}</p>
                    </div>

                    {/* Signos Vitales */}
                    <div className="ps-2 mb-3">
                        <p className="mb-1 fw-bold text-decoration-underline small">Signos Vitales:</p>
                        <table className="table table-bordered table-sm text-center small mb-1 w-100" style={{fontSize: '0.85rem'}}>
                            <thead className="table-light">
                                <tr><th>Peso</th><th>Talla</th><th>IMC</th><th>Temp</th><th>FC</th><th>FR</th><th>SatO2</th></tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>{consulta.examenFisico?.vitales?.peso} kg</td>
                                    <td>{consulta.examenFisico?.vitales?.talla} cm</td>
                                    <td>{consulta.examenFisico?.calculos?.imc?.valor || '-'}</td>
                                    <td>{consulta.examenFisico?.vitales?.temperatura} °C</td>
                                    <td>{consulta.examenFisico?.vitales?.fc}</td>
                                    <td>{consulta.examenFisico?.vitales?.fr}</td>
                                    <td>{consulta.examenFisico?.vitales?.spo2}%</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>

                    {/* Diagnóstico y Plan */}
                    <div className="ps-2 mb-3">
                         <p className="mb-1 fw-bold text-decoration-underline small">Diagnósticos:</p>
                         <ul className="small mb-1 ps-3">
                            {consulta.diagnostico?.cie10?.map((d:any, i:number) => (
                                <li key={i}><strong>{d.cie10}</strong> - {d.descripcion} ({d.tipo})</li>
                            ))}
                         </ul>
                    </div>

                    <div className="ps-2 border rounded p-2 bg-light">
                        <p className="mb-1 fw-bold text-danger small">PLAN Y TRATAMIENTO:</p>
                        <div className="small" style={{whiteSpace: 'pre-wrap'}}>{consulta.diagnostico?.plan || 'Sin indicaciones.'}</div>
                        <div className="d-flex justify-content-between mt-2 small border-top pt-1">
                             <span><strong>Próxima Cita:</strong> {consulta.diagnostico?.proximaCita || '-'}</span>
                             <span><strong>Pronóstico:</strong> {consulta.diagnostico?.pronostico || '-'}</span>
                        </div>
                    </div>
                </div>

                {/* 4. HISTORIAL PREVIO */}
                <div className="mt-5 pt-2">
                    <h6 className="fw-bold text-uppercase border-bottom pb-1 mb-2 bg-light px-2">3. Historial de Consultas Anteriores</h6>
                    {historialPrevio.length > 0 ? (
                        <table className="table table-striped table-sm small w-100">
                            <thead className="table-dark">
                                <tr>
                                    <th style={{width: '20%'}}>Fecha</th>
                                    <th style={{width: '50%'}}>Motivo Consulta</th>
                                    <th style={{width: '30%'}}>Médico</th>
                                </tr>
                            </thead>
                            <tbody>
                                {historialPrevio.map((hist: any, index: number) => (
                                    <tr key={index}>
                                        <td>{hist.fecha}</td>
                                        <td>{hist.motivo}</td>
                                        <td>{nombreDoctor}</td> 
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    ) : (
                        <p className="text-muted small ps-2">No existen consultas previas registradas.</p>
                    )}
                </div>

                {/* FIRMA */}
                <div className="row mt-5 pt-5">
                    <div className="col-4 offset-4 text-center">
                        <div className="border-top border-dark pt-1">
                            <p className="fw-bold mb-0 small">{nombreDoctor}</p>
                            <small className="text-muted" style={{fontSize: '0.7rem'}}>MÉDICO TRATANTE</small>
                        </div>
                    </div>
                </div>

            </div>
          </div>
        </div>
      </div>

      {/* ESTILOS PARA QUE AL IMPRIMIR SOLO SALGA LA HOJA BLANCA */}
      <style>{`
        @media print {
            .modal-header, .modal-footer, .btn { display: none !important; }
            .modal-body { padding: 0 !important; background: white !important; }
            .card, .modal-content { border: none !important; box-shadow: none !important; }
            body { background: white !important; }
            .page-break { page-break-before: always; }
        }
      `}</style>
    </div>
  );
}