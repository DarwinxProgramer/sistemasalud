import React from 'react';

type ApgarType = {
  apariencia: number;
  pulso: number;
  reflejos: number;
  tonoMuscular: number;
  respiracion: number;
};

type ComplicacionesType = {
  ninguna: boolean;
  sdr: boolean;
  ictericia: boolean;
  sepsis: boolean;
};

interface Props {
  isAntBlocked: boolean;
  productoGestacion: string;
  setProductoGestacion: (val: string) => void;
  edadGestacional: number | "";
  setEdadGestacional: (val: number | "") => void;
  viaParto: string;
  setViaParto: (val: string) => void;
  pesoNacimiento: number | "";
  setPesoNacimiento: (val: number | "") => void;
  tallaNacimiento: number | "";
  setTallaNacimiento: (val: number | "") => void;
  apgar: ApgarType;
  setApgar: React.Dispatch<React.SetStateAction<ApgarType>>;
  checksComplicaciones: ComplicacionesType;
  setChecksComplicaciones: React.Dispatch<React.SetStateAction<ComplicacionesType>>;
  descripcionComplicaciones: string;
  setDescripcionComplicaciones: (val: string) => void;
}

export const TabAntecedentesPerinatales: React.FC<Props> = ({
  isAntBlocked,
  productoGestacion,
  setProductoGestacion,
  edadGestacional,
  setEdadGestacional,
  viaParto,
  setViaParto,
  pesoNacimiento,
  setPesoNacimiento,
  tallaNacimiento,
  setTallaNacimiento,
  apgar,
  setApgar,
  checksComplicaciones,
  setChecksComplicaciones,
  descripcionComplicaciones,
  setDescripcionComplicaciones
}) => {

  // 🔥 TOTAL APGAR
  const totalApgar =
    Number(apgar.apariencia || 0) +
    Number(apgar.pulso || 0) +
    Number(apgar.reflejos || 0) +
    Number(apgar.tonoMuscular || 0) +
    Number(apgar.respiracion || 0);

  

  // 🔥 CLASIFICACIÓN CLÍNICA
  const clasificacionApgar =
    totalApgar >= 7 ? "NORMAL" :
    totalApgar >= 4 ? "INTERMEDIO" :
    "BAJO (REANIMACIÓN INMEDIATA)";

  const colorApgar =
    totalApgar >= 7 ?  "bg-success" :
    totalApgar >= 4 ? "bg-warning text-dark" :
    "bg-danger";

  const handleApgarChange = (campo: keyof ApgarType, valor: string) => {
    const numero = Number(valor);
    setApgar(prev => ({
      ...prev,
      [campo]: numero
    }));
  };

  const handleComplicacionChange = (campo: keyof ComplicacionesType, checked: boolean) => {
    if (campo === 'ninguna' && checked) {
      setChecksComplicaciones({ ninguna: true, sdr: false, ictericia: false, sepsis: false });
    } else {
      setChecksComplicaciones(prev => ({ ...prev, [campo]: checked, ninguna: false }));
    }
  };

  return (
    <div className="row g-3 w-100 m-0">
      <div className="col-12 border-bottom pb-2 mb-2">
        <h6 className="text-primary m-0 fw-bold">Antecedentes Perinatales</h6>
      </div>

      <div className="col-md-3">
        <label className="small fw-bold">Producto Gestación</label>
        <select className="form-select" disabled={isAntBlocked} value={productoGestacion} onChange={e => setProductoGestacion(e.target.value)}>
          <option value="">Seleccionar</option>
          <option>Única</option><option>Gemelar</option><option>Múltiple</option>
        </select>
      </div>

      <div className="col-md-2">
        <label className="small fw-bold">Edad Gest.</label>
        <input type="number" className="form-control" disabled={isAntBlocked} value={edadGestacional}
          onChange={e => setEdadGestacional(e.target.value === "" ? "" : Number(e.target.value))} />
      </div>

      <div className="col-md-3">
        <label className="small fw-bold">Vía Parto</label>
        <select className="form-select" disabled={isAntBlocked} value={viaParto} onChange={e => setViaParto(e.target.value)}>
          <option value="">Seleccionar</option>
          <option>Vaginal</option><option>Cesárea</option>
        </select>
      </div>

      <div className="col-md-2">
        <label className="small fw-bold">Peso (g)</label>
        <input type="number" className="form-control" disabled={isAntBlocked} value={pesoNacimiento}
          onChange={e => setPesoNacimiento(e.target.value === "" ? "" : Number(e.target.value))} />
      </div>

      <div className="col-md-2">
        <label className="small fw-bold">Talla (cm)</label>
        <input type="number" className="form-control" disabled={isAntBlocked} value={tallaNacimiento}
          onChange={e => setTallaNacimiento(e.target.value === "" ? "" : Number(e.target.value))} />
      </div>

      {/* APGAR */}
      <div className="col-12 mt-3">
        <div className="card bg-light border-0 shadow-sm">
          <div className="card-body">
            <div className="d-flex justify-content-between align-items-center mb-3">
              <span className="fw-bold text-primary">Prueba de Apgar</span>

              <div className="text-end">
                <span className={`badge fs-6 ${colorApgar}`}>
                  Total: {totalApgar} / 10
                </span>
                <div className="small mt-1">
                  Promedio: {clasificacionApgar}
                </div>
              </div>
            </div>

            <div className="row g-2 text-center">
              {[
                { key: 'apariencia', label: 'Apariencia' },
                { key: 'pulso', label: 'Pulso' },
                { key: 'reflejos', label: 'Reflejos' },
                { key: 'tonoMuscular', label: 'Tono' },
                { key: 'respiracion', label: 'Respiración' }
              ].map(({ key, label }) => (
                <div className="col" key={key}>
                  <label className="small d-block text-muted mb-1">{label}</label>
                  <select
                    className="form-select form-select-sm"
                    disabled={isAntBlocked}
                    value={apgar[key as keyof ApgarType]}
                    onChange={e => handleApgarChange(key as keyof ApgarType, e.target.value)}
                  >
                    <option value={0}>0</option>
                    <option value={1}>1</option>
                    <option value={2}>2</option>
                  </select>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>

      {/* COMPLICACIONES */}
      <div className="col-12 mt-3">
        <label className="small fw-bold mb-2">Complicaciones</label>
        <div className="d-flex gap-3 mb-2 flex-wrap">
          {Object.entries(checksComplicaciones).map(([key, val]) => (
            <div className="form-check" key={key}>
              <input className="form-check-input" type="checkbox" checked={val}
                onChange={e => handleComplicacionChange(key as keyof ComplicacionesType, e.target.checked)}
                disabled={isAntBlocked} />
              <label className="form-check-label small">{key.toUpperCase()}</label>
            </div>
          ))}
        </div>
        <textarea className="form-control" disabled={isAntBlocked || checksComplicaciones.ninguna} rows={2}
          placeholder="Descripción opcional..." value={descripcionComplicaciones}
          onChange={e => setDescripcionComplicaciones(e.target.value)} />
      </div>
    </div>
  );
};
