import React from 'react';

// Exportación nombrada exacta para que el Padre lo encuentre
export const AlertaAlergia = ({ tiene, descripcion }: { tiene: boolean, descripcion: string }) => {
    if (!tiene) return null;
    return (
        <div className="alert alert-danger rounded-0 m-0 d-flex align-items-center py-2 animate__animated animate__flash animate__infinite" 
             style={{ borderBottom: '2px solid darkred', zIndex: 1000 }}>
            <i className="bi bi-exclamation-triangle-fill fs-4 me-3"></i>
            <div>
                <strong className="d-block text-uppercase">¡Alerta Médica: Paciente Alérgico!</strong>
                <span className="small">{descripcion || "Verificar antecedentes personales inmediatamente."}</span>
            </div>
        </div>
    );
};