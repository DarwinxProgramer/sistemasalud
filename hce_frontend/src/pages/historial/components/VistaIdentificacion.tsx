import React from 'react';

export const VistaIdentificacion = ({ cedula, paciente }: any) => (
    <div className="card mb-4 border-0 shadow-sm bg-light">
        <div className="card-body p-3">
            <h6 className="text-muted small fw-bold mb-3 text-uppercase">Datos de Identificación (No Editable)</h6>
            <div className="row g-3 px-2">
                <div className="col-md-3"><strong>Cédula:</strong><br/> {cedula}</div>
                <div className="col-md-3"><strong>Nombres:</strong><br/> {paciente?.nombres || "—"}</div>
                <div className="col-md-3"><strong>Apellidos:</strong><br/> {paciente?.apellidos || "—"}</div>
                <div className="col-md-3"><strong>Fecha Nac:</strong><br/> {paciente?.fechaNacimiento || "—"}</div>
            </div>
        </div>
    </div>
);