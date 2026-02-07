import React from 'react';

export const TabAntecedentesPersonales = ({ 
    isAntBlocked, enfermedadesCronicas, setEnfermedadesCronicas, 
    hospitalizaciones, setHospitalizaciones,
    cirugias, setCirugias,
    alergias, setAlergias,
    familiares, setFamiliares,
    descripcionCronicas, setDescripcionCronicas
}: any) => {

    // Obtener fecha actual en formato YYYY-MM-DD para el max del input date
    const today = new Date().toISOString().split('T')[0];

    const handleFamiliaresChange = (key: string) => {
        // Si selecciona 'Ninguna', desmarcamos el resto
        if (key === 'Ninguna') {
            const resetFamiliares = Object.keys(familiares).reduce((acc: any, curr) => {
                acc[curr] = curr === 'Ninguna' ? !familiares['Ninguna'] : false;
                return acc;
            }, {});
            setFamiliares(resetFamiliares);
        } else {
            // Si selecciona cualquier otra, desmarcamos 'Ninguna'
            setFamiliares({
                ...familiares,
                [key]: !familiares[key],
                Ninguna: false
            });
        }
    };

    return (
        <div className="container-fluid p-2">
            <h6 className="fw-bold text-primary mb-3 border-bottom pb-2">Antecedentes Patológicos Personales</h6>
            
            {/* Enfermedades Crónicas */}
            <div className="mb-4">
                <label className="fw-bold small d-block mb-2 text-secondary">Enfermedades Crónicas</label>
                <div className="d-flex gap-3 flex-wrap">
                    {Object.keys(enfermedadesCronicas).map(k => (
                        <div className="form-check" key={k}>
                            <input type="checkbox" className="form-check-input" 
                                disabled={isAntBlocked} 
                                checked={enfermedadesCronicas[k]} 
                                onChange={() => setEnfermedadesCronicas({...enfermedadesCronicas, [k]: !enfermedadesCronicas[k]})} />
                            <label className="form-check-label small">{k}</label>
                        </div>
                    ))}
                </div>
            </div>

            {/* Hospitalizaciones, Cirugías y Alergias - Grid Responsivo */}
            <div className="row g-4 mb-4">
                {/* Hospitalizaciones */}
                <div className="col-12 col-md-4 border-md-end">
                    <div className="form-check form-switch mb-2">
                        <input className="form-check-input" type="checkbox" checked={hospitalizaciones.tiene} 
                            disabled={isAntBlocked} onChange={e => setHospitalizaciones({...hospitalizaciones, tiene: e.target.checked})} />
                        <label className="fw-bold small">Hospitalizaciones</label>
                    </div>
                    {hospitalizaciones.tiene && (
                        <div className="mt-2">
                            <label className="x-small text-muted d-block">Causa:</label>
                            <textarea className="form-control form-control-sm mb-2" placeholder="Describa la causa" 
                                value={hospitalizaciones.descripcion} onChange={e => setHospitalizaciones({...hospitalizaciones, descripcion: e.target.value})} />
                            <label className="x-small text-muted d-block">Fecha:</label>
                            <input type="date" className="form-control form-control-sm" max={today}
                                value={hospitalizaciones.fecha || ''} onChange={e => setHospitalizaciones({...hospitalizaciones, fecha: e.target.value})} />
                        </div>
                    )}
                </div>

                {/* Cirugías */}
                <div className="col-12 col-md-4 border-md-end">
                    <div className="form-check form-switch mb-2">
                        <input className="form-check-input" type="checkbox" checked={cirugias.tiene} 
                            disabled={isAntBlocked} onChange={e => setCirugias({...cirugias, tiene: e.target.checked})} />
                        <label className="fw-bold small">Cirugías Previas</label>
                    </div>
                    {cirugias.tiene && (
                        <div className="mt-2">
                            <label className="x-small text-muted d-block">Tipo de Cirugía:</label>
                            <textarea className="form-control form-control-sm mb-2" placeholder="Ej: Apendicectomía" 
                                value={cirugias.descripcion} onChange={e => setCirugias({...cirugias, descripcion: e.target.value})} />
                            <label className="x-small text-muted d-block">Fecha:</label>
                            <input type="date" className="form-control form-control-sm" max={today}
                                value={cirugias.fecha || ''} onChange={e => setCirugias({...cirugias, fecha: e.target.value})} />
                        </div>
                    )}
                </div>

                {/* Alergias */}
                <div className="col-12 col-md-4">
                    <div className="form-check form-switch mb-2">
                        <input className="form-check-input" type="checkbox" checked={alergias.tiene} 
                            disabled={isAntBlocked} onChange={e => setAlergias({...alergias, tiene: e.target.checked})} />
                        <label className="fw-bold small">Alergias</label>
                    </div>
                    {alergias.tiene && (
                        <div className="mt-2">
                            <label className="x-small text-muted d-block">Tipo/Alimento/Fármaco:</label>
                            <textarea className="form-control form-control-sm" 
                                placeholder="Ej: Penicilina, Mariscos..." 
                                value={alergias.descripcion} 
                                onChange={e => setAlergias({...alergias, descripcion: e.target.value})} />
                        </div>
                    )}
                </div>
            </div>

            {/* Antecedentes Familiares */}
            <h6 className="fw-bold text-primary mb-3 border-bottom pb-2">Antecedentes Familiares (1er Grado)</h6>
            <div className="d-flex gap-3 flex-wrap mb-3">
                {['HTA', 'Diabetes', 'Cáncer', 'Enfermedades Genéticas', 'Ninguna', 'Otros'].map(f => (
                    <div className="form-check" key={f}>
                        <input type="checkbox" className="form-check-input" 
                            disabled={isAntBlocked} 
                            id={`fam-${f}`}
                            checked={familiares[f] || false} 
                            onChange={() => handleFamiliaresChange(f)} />
                        <label className="form-check-label small" htmlFor={`fam-${f}`}>{f}</label>
                    </div>
                ))}
            </div>

            {/* Input especial para "Otros" (Estilo Diagnóstico) */}
            {familiares['Otros'] && (
                <div className="row g-2 mb-3 animate__animated animate__fadeIn">
                    <div className="col-4 col-md-2">
                        <input type="text" className="form-control form-control-sm" placeholder="CIE-10" disabled={isAntBlocked} />
                    </div>
                    <div className="col-8 col-md-10">
                        <input type="text" className="form-control form-control-sm" placeholder="Especifique otra enfermedad familiar" disabled={isAntBlocked} />
                    </div>
                </div>
            )}

            <textarea className="form-control w-100 mt-2" 
                disabled={isAntBlocked} rows={2} 
                placeholder="Otras observaciones familiares detalladas..." 
                value={descripcionCronicas} 
                onChange={e => setDescripcionCronicas(e.target.value)} 
            />
        </div>
    );
};
