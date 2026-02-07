import React from 'react';

export const ReporteCompletoHCE = ({ paciente }: any) => {
    if (!paciente) return <div className="p-5 text-center">Cargando datos del paciente...</div>;

    // Se obtiene la última consulta registrada para mostrar los datos actuales
    const ultimaConsulta = paciente.historiaClinica?.[paciente.historiaClinica.length - 1];

    const renderSeccion = (titulo: string, contenido: React.ReactNode) => (
        <div className="mb-4 border rounded shadow-sm bg-white overflow-hidden">
            <div className="bg-primary text-white px-3 py-2 fw-bold small text-uppercase">
                {titulo}
            </div>
            <div className="p-3 small">{contenido}</div>
        </div>
    );

    return (
        <div className="container-fluid p-3 bg-light" id="reporte-hce">
            <div className="d-flex justify-content-between align-items-center mb-3">
                <h5 className="text-primary fw-bold">REPORTE CLÍNICO - VISTA PREVIA</h5>
                <button className="btn btn-sm btn-outline-secondary d-print-none" onClick={() => window.print()}>
                    <i className="bi bi-printer me-1"></i> Imprimir
                </button>
            </div>

            {/* 1 IDENTIFICACIÓN (Siempre visible) */}
            {renderSeccion("1. Identificación del Paciente", (
                <div className="row g-2 text-dark">
                    <div className="col-md-3"><strong>Cédula:</strong> {paciente.cedula}</div>
                    <div className="col-md-3"><strong>Nombre:</strong> {paciente.apellidos} {paciente.nombres}</div>
                    <div className="col-md-3"><strong>Fecha Nac:</strong> {paciente.fechaNacimiento}</div>
                    <div className="col-md-3"><strong>Sexo / Sangre:</strong> {paciente.sexo} / {paciente.tipoSangre}</div>
                    <div className="col-md-6"><strong>Contacto Responsable:</strong> {paciente.filiacion?.nombreResponsable || "No registrado"}</div>
                    <div className="col-md-3"><strong>Parentesco:</strong> {paciente.filiacion?.parentesco || "—"}</div>
                    <div className="col-md-3"><strong>Teléfono:</strong> {paciente.filiacion?.telefonoContacto || "—"}</div>
                </div>
            ))}

            {/* Validación de consultas */}
            {!ultimaConsulta ? (
                <div className="alert alert-info">El paciente no registra consultas médicas previas.</div>
            ) : (
                <>
                    {/* 2 MOTIVO Y ENFERMEDAD */}
                    {renderSeccion("2. Motivo de Consulta y Enfermedad Actual", (
                        <>
                            <p><strong>Motivo:</strong> {ultimaConsulta.motivo}</p>
                            <p><strong>Enfermedad Actual:</strong> {ultimaConsulta.enfermedadActual}</p>
                            <p className="text-muted mb-0 small"><strong>Atendido el:</strong> {ultimaConsulta.fecha} {ultimaConsulta.hora}</p>
                        </>
                    ))}

                    {/* 3 ANTECEDENTES PERINATALES */}
                    {renderSeccion("3. Antecedentes Perinatales", (
                        <div className="row g-2">
                            <div className="col-md-4"><strong>Producto:</strong> {ultimaConsulta.antecedentes?.perinatales?.productoGestacion}</div>
                            <div className="col-md-4"><strong>Vía Parto:</strong> {ultimaConsulta.antecedentes?.perinatales?.viaParto}</div>
                            <div className="col-md-4"><strong>Edad Gestacional:</strong> {ultimaConsulta.antecedentes?.perinatales?.edadGestacional} semanas</div>
                            <div className="col-md-4"><strong>Peso RN:</strong> {ultimaConsulta.antecedentes?.perinatales?.pesoNacimiento} g</div>
                            <div className="col-md-4"><strong>Talla RN:</strong> {ultimaConsulta.antecedentes?.perinatales?.tallaNacimiento} cm</div>
                        </div>
                    ))}

                    {/* 4 ANTECEDENTES PERSONALES */}
                    {renderSeccion("4. Antecedentes Patológicos Personales", (
                        <div className="row g-2">
                            <div className="col-12"><strong>Enfermedades Crónicas:</strong> {
                                Object.entries(ultimaConsulta.antecedentes?.personales?.enfermedadesCronicas || {})
                                    .filter(([_, v]) => v).map(([k]) => k).join(", ") || "Ninguna"
                            }</div>
                            <div className="col-12 text-danger fw-bold"><strong>Alergias:</strong> {
                                ultimaConsulta.antecedentes?.personales?.alergias?.tiene
                                    ? ultimaConsulta.antecedentes.personales.alergias.descripcion
                                    : "Sin alergias conocidas"
                            }</div>
                        </div>
                    ))}

                    {/* 7 EXAMEN FÍSICO */}
                    {renderSeccion("5. Examen Físico", (
                        <div className="row g-2">
                            <div className="col-md-4"><strong>Peso:</strong> {ultimaConsulta.examenFisico?.vitales?.peso} kg</div>
                            <div className="col-md-4"><strong>Talla:</strong> {ultimaConsulta.examenFisico?.vitales?.talla} cm</div>
                            <div className="col-md-4"><strong>Temperatura:</strong> {ultimaConsulta.examenFisico?.vitales?.temperatura} °C</div>
                            <div className="col-12 border-top mt-2 pt-2"><strong>Evolución:</strong> {ultimaConsulta.examenFisico?.evolucion}</div>
                        </div>
                    ))}

                    {/* 8 DIAGNÓSTICO */}
                    {renderSeccion("6. Diagnóstico y Plan", (
                        <>
                            <p className="fw-bold mb-1">Diagnóstico Principal:</p>
                            <p>{ultimaConsulta.diagnostico?.principal?.descripcion} ({ultimaConsulta.diagnostico?.principal?.cie10})</p>
                            <p><strong>Plan Farmacológico:</strong> {ultimaConsulta.diagnostico?.plan?.farmacologico?.esquema || "No prescrito"}</p>
                        </>
                    ))}
                </>
            )}
        </div>
    );
};
