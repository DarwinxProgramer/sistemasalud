import React from 'react';

interface Props {
  signosVitales: any;
  setSignosVitales: React.Dispatch<React.SetStateAction<any>>;
  examenSegmentario: any;
  setExamenSegmentario: React.Dispatch<React.SetStateAction<any>>;
  zPesoEdad: any;
  zTallaEdad: any;
  zIMCEdad: any;
  resultadoIMC?: any;
  evolucionClinica: string;
  setEvolucionClinica: (val: string) => void;
  paciente: any; // Se añade para corregir error TS2322
}

export const TabExamenFisico: React.FC<Props> = ({
  signosVitales,
  setSignosVitales,
  examenSegmentario,
  setExamenSegmentario,
  zPesoEdad,
  zTallaEdad,
  zIMCEdad,
  resultadoIMC,
  evolucionClinica,
  setEvolucionClinica,
  paciente
}) => {

  const renderValue = (val: any) => {
    if (val === null || val === undefined) return "0.00";
    if (typeof val === 'object') return val.valor ?? "0.00";
    const num = parseFloat(val);
    return isNaN(num) ? "0.00" : num.toFixed(2);
  };

  // Gráfica de Curva Z estilo OMS (Indicador visual)
  const CurvaZGrafica = ({ valor }: { valor: any }) => {
    const v = parseFloat(valor?.valor || valor || 0);
    const posicion = Math.min(Math.max(((v + 3) / 6) * 100, 0), 100);
    
    return (
      <div className="mt-2 mb-3 px-1 w-100">
        <div style={{ 
          position: 'relative', height: '12px', width: '100%', 
          display: 'flex', borderRadius: '6px', border: '1px solid #ccc', background: '#eee' 
        }}>
          <div style={{ width: '16.6%', backgroundColor: '#d9534f', borderRadius: '6px 0 0 6px' }}></div> 
          <div style={{ width: '16.6%', backgroundColor: '#f0ad4e' }}></div> 
          <div style={{ width: '33.4%', backgroundColor: '#5cb85c' }}></div> 
          <div style={{ width: '16.6%', backgroundColor: '#f0ad4e' }}></div> 
          <div style={{ width: '16.6%', backgroundColor: '#d9534f', borderRadius: '0 6px 6px 0' }}></div> 
          <div style={{
            position: 'absolute', left: `${posicion}%`, top: '50%',
            width: '10px', height: '10px', backgroundColor: '#000',
            borderRadius: '50%', border: '2px solid #fff',
            transform: 'translate(-50%, -50%)', zIndex: 10
          }}></div>
        </div>
      </div>
    );
  };

  return (
    <div className="row g-3 w-100 m-0">
      <div className="col-12 border-bottom pb-2">
        <h6 className="text-primary m-0 fw-bold">A. Signos Vitales y Antropometría</h6>
      </div>

      {/* SIGNOS VITALES RESPONSIVOS */}
      <div className="col-12">
        <div className="card border-0 shadow-sm bg-white p-3">
          <div className="row g-2">
            {[
              { id: 'peso', label: 'Peso (Kg)' }, { id: 'talla', label: 'Talla (cm)' },
              { id: 'perimetroCefalico', label: 'P. Cefálico' }, { id: 'temperatura', label: 'Temp' },
              { id: 'fc', label: 'FC' }, { id: 'fr', label: 'FR' },
              { id: 'paSistolica', label: 'PA Sist.' }, { id: 'paDiastolica', label: 'PA Diast.' },
              { id: 'spo2', label: 'SpO2' }
            ].map((campo) => (
              <div className="col-6 col-sm-4 col-md-2" key={campo.id}>
                <label className="small fw-bold text-muted" style={{fontSize: '11px'}}>{campo.label}</label>
                <input 
                  type="number" 
                  className="form-control form-control-sm" 
                  value={signosVitales?.[campo.id] ?? ''} 
                  onChange={e => setSignosVitales({...signosVitales, [campo.id]: e.target.value})} 
                />
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* EVALUACIÓN NUTRICIONAL */}
      <div className="col-12 mt-2">
        <div className="card border-0 shadow-sm bg-light p-3">
          <label className="fw-bold text-secondary small d-block mb-2 text-center">EVALUACIÓN NUTRICIONAL (Z-SCORE)</label>
          <div className="row text-center g-3">
            <div className="col-12 col-md-3 border-end">
              <div className="small text-muted">IMC</div>
              <div className="fw-bold fs-5">{renderValue(resultadoIMC)}</div>
              <span className="badge rounded-pill" style={{ backgroundColor: resultadoIMC?.color || '#eee', color: '#000' }}>
                {resultadoIMC?.interpretacion || 'N/A'}
              </span>
            </div>
            {[
              { label: 'Z-Peso/E', value: zPesoEdad },
              { label: 'Z-Talla/E', value: zTallaEdad },
              { label: 'Z-IMC/E', value: zIMCEdad }
            ].map((item, idx) => (
              <div className="col-4 col-md-3" key={idx}>
                <div className="small text-muted fw-bold">{item.label}</div>
                <div className="fw-bold text-primary">{renderValue(item.value)}</div>
                <CurvaZGrafica valor={item.value} />
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="col-12 mt-3 border-bottom pb-2">
        <h6 className="text-primary m-0 fw-bold">B. Examen Físico Segmentario</h6>
      </div>

      <div className="col-12">
        <div className="card border-0 shadow-sm p-3 bg-light">
          <div className="row g-4">
            {/* Aspecto General */}
            <div className="col-12 col-md-4">
              <label className="small fw-bold text-success border-bottom d-block mb-2">Aspecto General</label>
              <div className="d-flex flex-wrap gap-2">
                {['Consciente', 'Alerta', 'Activo', 'Decaído'].map(item => (
                  <div className="form-check" key={item}>
                    <input className="form-check-input" type="checkbox" 
                      checked={examenSegmentario?.aspecto?.[item] || false} 
                      onChange={() => setExamenSegmentario({...examenSegmentario, aspecto: {...examenSegmentario.aspecto, [item]: !examenSegmentario.aspecto[item]}})} />
                    <label className="form-check-label small">{item}</label>
                  </div>
                ))}
              </div>
            </div>

            {/* Piel y Faneras */}
            <div className="col-12 col-md-4">
              <label className="small fw-bold text-success border-bottom d-block mb-2">Piel y Faneras</label>
              <div className="d-flex flex-wrap gap-2">
                {['Ictericia', 'Cianosis', 'Rash', 'Normal'].map(item => (
                  <div className="form-check" key={item}>
                    <input className="form-check-input" type="checkbox" 
                      checked={examenSegmentario?.piel?.[item] || false} 
                      onChange={() => setExamenSegmentario({...examenSegmentario, piel: {...examenSegmentario.piel, [item]: !examenSegmentario.piel[item]}})} />
                    <label className="form-check-label small">{item}</label>
                  </div>
                ))}
              </div>
            </div>

            {/* Cabeza y Cuello */}
            <div className="col-12 col-md-4">
              <label className="small fw-bold text-success border-bottom d-block mb-2">Cabeza y Cuello</label>
              <div className="d-flex flex-wrap gap-2">
                {['Fontanela Anterior', 'Adenopatías', 'Normal'].map(item => (
                  <div className="form-check" key={item}>
                    <input className="form-check-input" type="checkbox" 
                      checked={examenSegmentario?.cabeza?.[item] || false} 
                      onChange={() => setExamenSegmentario({...examenSegmentario, cabeza: {...examenSegmentario.cabeza, [item]: !examenSegmentario.cabeza[item]}})} />
                    <label className="form-check-label small">{item}</label>
                  </div>
                ))}
              </div>
            </div>

            {/* Cardiopulmonar */}
            <div className="col-12 col-md-6">
              <label className="small fw-bold text-success border-bottom d-block mb-2">Cardiopulmonar</label>
              <div className="d-flex flex-wrap gap-3">
                {['Ruidos cardiacos', 'Murmullo vesicular', 'Soplos', 'Crepitantes'].map(item => (
                  <div className="form-check" key={item}>
                    <input className="form-check-input" type="checkbox" 
                      checked={examenSegmentario?.cardio?.[item] || false} 
                      onChange={() => setExamenSegmentario({...examenSegmentario, cardio: {...examenSegmentario.cardio, [item]: !examenSegmentario.cardio[item]}})} />
                    <label className="form-check-label small">{item}</label>
                  </div>
                ))}
              </div>
            </div>

            {/* Abdomen */}
            <div className="col-12 col-md-6">
              <label className="small fw-bold text-success border-bottom d-block mb-2">Abdomen</label>
              <div className="d-flex flex-wrap gap-3">
                {['Blando', 'Depresible', 'Hepatomegalia', 'Esplenomegalia'].map(item => (
                  <div className="form-check" key={item}>
                    <input className="form-check-input" type="checkbox" 
                      checked={examenSegmentario?.abdomen?.[item] || false} 
                      onChange={() => setExamenSegmentario({...examenSegmentario, abdomen: {...examenSegmentario.abdomen, [item]: !examenSegmentario.abdomen[item]}})} />
                    <label className="form-check-label small">{item}</label>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="col-12 mt-3">
        <label className="small fw-bold">Evolución Clínica / Hallazgos Adicionales</label>
        <textarea className="form-control" rows={3} value={evolucionClinica} 
          onChange={e => setEvolucionClinica(e.target.value)} 
          placeholder="Describa el resumen del curso de la enfermedad..." />
      </div>
    </div>
  );
};
