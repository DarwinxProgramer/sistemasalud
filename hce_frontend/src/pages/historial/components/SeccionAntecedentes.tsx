import React from 'react';
import { TabAntecedentesPerinatales } from '../TabAntecedentesPerinatales';
import { TabAntecedentesPersonales } from '../TabAntecedentesPersonales';
import { TabInmunizaciones } from '../TabInmunizaciones';
import { TabDesarrolloPsicomotor } from '../TabDesarrolloPsicomotor';

export const SeccionAntecedentes = ({ bloquear, setBloquear, perinatales, personales, inmunizaciones, desarrollo }: any) => (
    <div className="card mb-4 border-0 shadow-sm">
        <div className="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h6 className="m-0 text-primary fw-bold text-uppercase small">Historial de Antecedentes</h6>
            <button className={`btn btn-sm ${bloquear ? 'btn-outline-danger' : 'btn-danger shadow'}`} onClick={() => setBloquear(!bloquear)}>
                {bloquear ? "🔓 Habilitar Edición" : "🔒 Bloquear Edición"}
            </button>
        </div>
        <div className="card-body p-0">
            <div className="row g-0">
                <div className="col-12 p-4 border-bottom">
                    <TabAntecedentesPerinatales isAntBlocked={bloquear} {...perinatales} />
                </div>
                <div className="col-12 col-xl-6 p-4 border-end border-bottom">
                    <TabAntecedentesPersonales isAntBlocked={bloquear} {...personales} />
                </div>
                <div className="col-12 col-xl-6 p-4 border-bottom">
                    <TabInmunizaciones isAntBlocked={bloquear} {...inmunizaciones} />
                </div>
                <div className="col-12 p-4">
                    <TabDesarrolloPsicomotor isAntBlocked={bloquear} {...desarrollo} />
                </div>
            </div>
        </div>
    </div>
);