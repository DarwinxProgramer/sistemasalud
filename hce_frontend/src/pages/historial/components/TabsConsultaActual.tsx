import React from 'react';
import { TabAnamnesis } from '../TabAnamnesis'; 
import { TabExamenFisico } from '../tabExamenFisico'; 
import { TabDiagnostico } from '../TabDiagnostico'; 

export const TabsConsultaActual = ({ tabActiva, setTabActiva, anamnesis, fisico, diagnostico }: any) => (
    <div className="mt-5">
        <ul className="nav nav-pills mb-4 bg-light p-2 rounded shadow-sm justify-content-center">
            {[{ id: 'anamnesis', label: 'Anamnesis' }, { id: 'fisico', label: 'Examen Físico' }, { id: 'diagnostico', label: 'Diagnóstico' }].map((t) => (
                <li className="nav-item" key={t.id}>
                    <button className={`nav-link px-4 ${tabActiva === t.id ? 'active shadow-sm' : 'text-secondary'}`} onClick={() => setTabActiva(t.id)}>{t.label}</button>
                </li>
            ))}
        </ul>
        <div className="tab-content border rounded p-4 bg-white shadow-sm mb-5">
            {tabActiva === 'anamnesis' && <TabAnamnesis {...anamnesis} />}
            {tabActiva === 'fisico' && (
                <TabExamenFisico 
                    signosVitales={fisico.signosVitales} setSignosVitales={fisico.setSignosVitales} 
                    examenSegmentario={fisico.examenSegmentario} setExamenSegmentario={fisico.setExamenSegmentario}
                    zPesoEdad={fisico.zP} zTallaEdad={fisico.zT} zIMCEdad={fisico.zI} resultadoIMC={fisico.resIMC} 
                    evolucionClinica={fisico.evolucionClinica} setEvolucionClinica={fisico.setEvolucionClinica} 
                    paciente={fisico.pacienteActual}
                />
            )}
            {tabActiva === 'diagnostico' && (
                <TabDiagnostico 
                    diagnosticoPrincipal={diagnostico.diagnosticoPrincipal} setDiagnosticoPrincipal={diagnostico.setDiagnosticoPrincipal}
                    secundarios={diagnostico.diagnosticosSecundarios} setSecundarios={diagnostico.setDiagnosticosSecundarios}
                    estudios={diagnostico.estudiosSolicitados} setEstudios={diagnostico.setEstudiosSolicitados}
                    resultados={diagnostico.resultadosExamenes} setResultados={diagnostico.setResultadosExamenes}
                    planFarmacologico={diagnostico.planFarmacologico} setPlanFarmacologico={diagnostico.setPlanFarmacologico}
                    planNoFarmacologico={diagnostico.planNoFarmacologico} setPlanNoFarmacologico={diagnostico.setPlanNoFarmacologico}
                    pronostico={diagnostico.pronostico} setPronostico={diagnostico.setPronostico}
                    proximaCita={diagnostico.proximaCita} handleProximaCitaChange={(e:any)=>diagnostico.setProximaCita(e.target.value)}
                    handleGuardar={diagnostico.handleGuardar}
                />
            )}
        </div>
    </div>
);
