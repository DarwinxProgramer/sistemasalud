export const TabAnamnesis = ({ motivoConsulta, setMotivoConsulta, enfermedadActual, setEnfermedadActual }: any) => (
    <>
    <div className="alert alert-info py-2 small mb-3 w-100"><i className="bi bi-info-circle me-2"></i> Anamnesis y Motivo de Consulta</div>
    <div className="mb-3 w-100"><label className="fw-bold">A. Motivo de Consulta</label><textarea className="form-control w-100" rows={2} value={motivoConsulta} onChange={e=>setMotivoConsulta(e.target.value)}/></div>
    <div className="mb-3 w-100"><label className="fw-bold">B. Enfermedad Actual</label><textarea className="form-control w-100" rows={10} value={enfermedadActual} onChange={e=>setEnfermedadActual(e.target.value)}/></div>
    </>
);