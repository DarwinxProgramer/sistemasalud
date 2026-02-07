import React from 'react';

interface Props {
  // Diagnósticos
  diagnosticoPrincipal: {
    cie10: string;
    descripcion: string;
    tipo: 'Presuntivo' | 'Definitivo';
  };
  setDiagnosticoPrincipal: React.Dispatch<React.SetStateAction<any>>;
  secundarios: any[];
  setSecundarios: React.Dispatch<React.SetStateAction<any[]>>;

  // Estudios y Laboratorios
  estudios: string;
  setEstudios: (val: string) => void;
  resultados: any[];
  setResultados: React.Dispatch<React.SetStateAction<any[]>>;

  // Plan Terapéutico
  planFarmacologico: {
    esquema: string;
    viaVenosa: string;
    viaOral: string;
  };
  setPlanFarmacologico: React.Dispatch<React.SetStateAction<any>>;
  planNoFarmacologico: {
    hidratacion: boolean;
    dieta: boolean;
    oxigeno: boolean;
    fisio: boolean;
    otros: string;
  };
  setPlanNoFarmacologico: React.Dispatch<React.SetStateAction<any>>;

  // Cierre
  pronostico: string;
  setPronostico: (val: string) => void;
  proximaCita: string;
  handleProximaCitaChange: (e: any) => void;
  handleGuardar: () => void;
}

export const TabDiagnostico: React.FC<Props> = ({
  diagnosticoPrincipal,
  setDiagnosticoPrincipal,
  secundarios,
  setSecundarios,
  estudios,
  setEstudios,
  resultados,
  setResultados,
  planFarmacologico,
  setPlanFarmacologico,
  planNoFarmacologico,
  setPlanNoFarmacologico,
  pronostico,
  setPronostico,
  proximaCita,
  handleProximaCitaChange,
  handleGuardar
}) => {

  // Función para añadir una fila vacía a la tabla de resultados
  const agregarFilaExamen = () => {
    setResultados([...resultados, { examen: '', tipo: 'Sangre', resultado: '' }]);
  };

  // Función para añadir diagnósticos secundarios
  const agregarDiagnosticoSecundario = () => {
    setSecundarios([...secundarios, { cie10: '', descripcion: '' }]);
  };

  return (
    <div className="row g-4 w-100 m-0">
      {/* SECCIÓN A: DIAGNÓSTICOS */}
      <div className="col-12 border-bottom pb-2">
        <h6 className="text-primary m-0 fw-bold">A. Diagnósticos (CIE-10/11)</h6>
      </div>

      <div className="col-12">
        <div className="card shadow-sm border-0 bg-light p-3">
          <label className="small fw-bold mb-2 text-uppercase text-primary">Diagnóstico Principal</label>
          <div className="row g-2">
            <div className="col-md-2">
              <input 
                type="text" 
                className="form-control" 
                placeholder="Código CIE-10" 
                value={diagnosticoPrincipal.cie10} 
                onChange={(e) => setDiagnosticoPrincipal({...diagnosticoPrincipal, cie10: e.target.value})} 
              />
            </div>
            <div className="col-md-7">
              <input 
                type="text" 
                className="form-control" 
                placeholder="Impresión Diagnóstica / Justificación" 
                value={diagnosticoPrincipal.descripcion} 
                onChange={(e) => setDiagnosticoPrincipal({...diagnosticoPrincipal, descripcion: e.target.value})} 
              />
            </div>
            <div className="col-md-3">
              <select 
                className="form-select fw-bold" 
                value={diagnosticoPrincipal.tipo} 
                onChange={(e) => setDiagnosticoPrincipal({...diagnosticoPrincipal, tipo: e.target.value})}
              >
                <option value="Presuntivo">PRESUNTIVO</option>
                <option value="Definitivo">DEFINITIVO</option>
              </select>
            </div>
          </div>

          <div className="mt-3">
            <div className="d-flex justify-content-between align-items-center mb-2">
              <label className="small fw-bold text-uppercase text-secondary">Diagnósticos Secundarios</label>
              <button className="btn btn-sm btn-link text-decoration-none p-0" onClick={agregarDiagnosticoSecundario}>
                <i className="bi bi-plus-circle"></i> Agregar Secundario
              </button>
            </div>
            {secundarios.map((diag, idx) => (
              <div className="row g-2 mb-2" key={idx}>
                <div className="col-md-2">
                  <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    placeholder="CIE-10" 
                    value={diag.cie10} 
                    onChange={(e) => {
                      const newDiags = [...secundarios];
                      newDiags[idx].cie10 = e.target.value;
                      setSecundarios(newDiags);
                    }} 
                  />
                </div>
                <div className="col-md-10">
                  <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    placeholder="Descripción" 
                    value={diag.descripcion} 
                    onChange={(e) => {
                      const newDiags = [...secundarios];
                      newDiags[idx].descripcion = e.target.value;
                      setSecundarios(newDiags);
                    }} 
                  />
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* SECCIÓN B: ESTUDIOS Y LABORATORIOS */}
      <div className="col-md-6 border-end">
        <h6 className="text-primary fw-bold border-bottom pb-2">B. Estudios y Laboratorios</h6>
        <label className="small fw-bold mt-2">Estudios Solicitados (Texto libre)</label>
        <textarea 
          className="form-control mb-3" 
          rows={2} 
          value={estudios} 
          onChange={(e) => setEstudios(e.target.value)} 
          placeholder="Ej: Biometría hemática, Rx de tórax, etc." 
        />
        
        <div className="d-flex justify-content-between align-items-center mb-2">
          <label className="small fw-bold">Tabla de Resultados Relevantes</label>
          <button className="btn btn-sm btn-outline-primary" onClick={agregarFilaExamen}>
            <i className="bi bi-plus-circle me-1"></i> Añadir Examen
          </button>
        </div>
        
        <div className="table-responsive border rounded bg-white" style={{maxHeight: '200px'}}>
          <table className="table table-sm table-hover m-0">
            <thead className="table-light small">
              <tr>
                <th>Examen (Lab/Imagen)</th>
                <th>Tipo</th>
                <th>Resultado</th>
              </tr>
            </thead>
            <tbody>
              {resultados.map((r, i) => (
                <tr key={i}>
                  <td>
                    <input 
                      type="text" 
                      className="form-control form-control-sm border-0" 
                      placeholder="Nombre del examen"
                      value={r.examen} 
                      onChange={(e) => {
                        const newRes = [...resultados];
                        newRes[i].examen = e.target.value;
                        setResultados(newRes);
                      }} 
                    />
                  </td>
                  <td>
                    <select 
                      className="form-select form-select-sm border-0" 
                      value={r.tipo} 
                      onChange={(e) => {
                        const newRes = [...resultados];
                        newRes[i].tipo = e.target.value;
                        setResultados(newRes);
                      }}
                    >
                      <option value="Sangre">Sangre</option>
                      <option value="Heces">Heces</option>
                      <option value="Orina">Orina</option>
                      <option value="Imagen">Imagen</option>
                    </select>
                  </td>
                  <td>
                    <input 
                      type="text" 
                      className="form-control form-control-sm border-0" 
                      placeholder="Valor/Hallazgo"
                      value={r.resultado} 
                      onChange={(e) => {
                        const newRes = [...resultados];
                        newRes[i].resultado = e.target.value;
                        setResultados(newRes);
                      }} 
                    />
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* SECCIÓN C: PLAN TERAPÉUTICO */}
      <div className="col-md-6">
        <h6 className="text-primary fw-bold border-bottom pb-2">C. Plan Terapéutico</h6>
        
        <label className="small fw-bold mt-2">Manejo Farmacológico</label>
        <textarea 
          className="form-control form-control-sm mb-2" 
          rows={2} 
          value={planFarmacologico.esquema} 
          onChange={(e) => setPlanFarmacologico({...planFarmacologico, esquema: e.target.value})} 
          placeholder="Medicación detallada: Fármaco, Dosis, Frecuencia, Duración..." 
        />
        
        <div className="row g-2 mb-3">
          <div className="col-6">
            <div className="input-group input-group-sm">
              <span className="input-group-text bg-white small">Vía Venosa</span>
              <input 
                type="text" 
                className="form-control" 
                placeholder="Indicaciones" 
                value={planFarmacologico.viaVenosa} 
                onChange={(e) => setPlanFarmacologico({...planFarmacologico, viaVenosa: e.target.value})} 
              />
            </div>
          </div>
          <div className="col-6">
            <div className="input-group input-group-sm">
              <span className="input-group-text bg-white small">Vía Oral</span>
              <input 
                type="text" 
                className="form-control" 
                placeholder="Indicaciones" 
                value={planFarmacologico.viaOral} 
                onChange={(e) => setPlanFarmacologico({...planFarmacologico, viaOral: e.target.value})} 
              />
            </div>
          </div>
        </div>

        <label className="small fw-bold">Manejo No Farmacológico / Medidas Higiénico-Dietéticas</label>
        <div className="card p-2 bg-light border-0 mb-2">
          <div className="d-flex flex-wrap gap-3">
            {['Hidratacion', 'Dieta', 'Oxigeno', 'Fisio'].map((item) => (
              <div className="form-check" key={item}>
                <input 
                  className="form-check-input" 
                  type="checkbox" 
                  id={`check-${item}`}
                  checked={(planNoFarmacologico as any)[item.toLowerCase()]} 
                  onChange={(e) => setPlanNoFarmacologico({...planNoFarmacologico, [item.toLowerCase()]: e.target.checked})}
                />
                <label className="form-check-label small fw-bold" htmlFor={`check-${item}`}>{item}</label>
              </div>
            ))}
          </div>
        </div>
        <input 
          type="text" 
          className="form-control form-control-sm" 
          placeholder="Otras medidas (Fisioterapia, cuidados, etc.)" 
          value={planNoFarmacologico.otros}
          onChange={(e) => setPlanNoFarmacologico({...planNoFarmacologico, otros: e.target.value})}
        />
      </div>

      {/* PRONÓSTICO Y FECHA */}
      <div className="col-12 mt-2">
        <div className="row g-3 align-items-end border-top pt-3">
          <div className="col-md-4">
            <label className="small fw-bold text-primary text-uppercase">Pronóstico del Paciente</label>
            <select 
              className="form-select border-primary fw-bold text-primary" 
              value={pronostico} 
              onChange={(e) => setPronostico(e.target.value)}
            >
              <option value="Bueno">BUENO</option>
              <option value="Reservado">RESERVADO</option>
              <option value="Malo">MALO</option>
            </select>
          </div>
          
          <div className="col-md-4">
            <label className="small fw-bold text-danger text-uppercase">Fecha Próxima Cita</label>
            <input 
              type="date" 
              className="form-control border-danger fw-bold text-danger" 
              value={proximaCita} 
              onChange={handleProximaCitaChange} 
            />
          </div>

          <div className="col-md-4 text-end">
            <button 
              className="btn btn-primary btn-lg w-100 fw-bold shadow-sm" 
              onClick={handleGuardar}
            >
              <i className="bi bi-cloud-check-fill me-2"></i> FINALIZAR Y GUARDAR CONSULTA
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
