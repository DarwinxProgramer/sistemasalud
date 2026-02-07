import React from 'react';

export const TabDesarrolloPsicomotor = ({ isAntBlocked, desarrollo, setDesarrollo, alimentacion, setAlimentacion }: any) => (
    <div className="row g-3 w-100 m-0 p-2">
        <div className="col-12 border-bottom pb-2 mb-2">
            <h6 className="fw-bold text-primary m-0 d-flex justify-content-between">
                Desarrollo Psicomotor
                <div className="form-check">
                    <input className="form-check-input" type="checkbox" checked={desarrollo.desconoce} onChange={e => setDesarrollo({...desarrollo, desconoce: e.target.checked})} />
                    <label className="form-check-label small text-muted">Padre desconoce/no recuerda</label>
                </div>
            </h6>
        </div>
        
        {!desarrollo.desconoce && (
            <>
                <div className="col-md-3"><label className="small fw-bold">Sostén Cefálico (meses)</label><input type="number" className="form-control" disabled={isAntBlocked} value={desarrollo.sostenCefalico} onChange={e=>setDesarrollo({...desarrollo, sostenCefalico:e.target.value})} /></div>
                <div className="col-md-3"><label className="small fw-bold">Sedestación (meses)</label><input type="number" className="form-control" disabled={isAntBlocked} value={desarrollo.sedestacion} onChange={e=>setDesarrollo({...desarrollo, sedestacion:e.target.value})} /></div>
                <div className="col-md-3"><label className="small fw-bold">Deambulación (meses)</label><input type="number" className="form-control" disabled={isAntBlocked} value={desarrollo.deambulacion} onChange={e=>setDesarrollo({...desarrollo, deambulacion:e.target.value})} /></div>
                <div className="col-md-3"><label className="small fw-bold">Lenguaje (meses)</label><input type="number" className="form-control" disabled={isAntBlocked} value={desarrollo.lenguaje} onChange={e=>setDesarrollo({...desarrollo, lenguaje:e.target.value})} /></div>
            </>
        )}

        <div className="col-12 mt-4"><h6 className="fw-bold text-secondary border-bottom pb-2">Alimentación</h6>
            <div className="row g-3">
                <div className="col-md-4 border-end">
                    <label className="small fw-bold">Lactancia Materna Exclusiva</label>
                    <div className="input-group input-group-sm">
                        <span className="input-group-text">Duración</span>
                        <input type="text" className="form-control" placeholder="Meses" value={alimentacion.lactancia.duracion} onChange={e => setAlimentacion({...alimentacion, lactancia: {...alimentacion.lactancia, duracion: e.target.value}})} />
                    </div>
                </div>
                <div className="col-md-4 border-end">
                    <label className="small fw-bold">Fórmula</label>
                    <input type="text" className="form-control form-control-sm" placeholder="Tipo de fórmula" value={alimentacion.formula.tipo} onChange={e => setAlimentacion({...alimentacion, formula: {...alimentacion.formula, tipo: e.target.value}})} />
                </div>
                <div className="col-md-4">
                    <label className="small fw-bold">Ablactación</label>
                    <input type="text" className="form-control form-control-sm" placeholder="Edad de inicio" value={alimentacion.ablactacion.edadInicio} onChange={e => setAlimentacion({...alimentacion, ablactacion: {...alimentacion.ablactacion, edadInicio: e.target.value}})} />
                </div>
            </div>
        </div>
    </div>
);
