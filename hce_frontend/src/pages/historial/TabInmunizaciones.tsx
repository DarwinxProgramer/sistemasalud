export const TabInmunizaciones = ({ isAntBlocked, estadoVacunacion, setEstadoVacunacion }: any) => (
    <div className="mb-3 w-100">
        <h6 className="text-primary border-bottom pb-2">Inmunizaciones</h6>
        <label className="mb-2 d-block small">Estado de Vacunación según esquema nacional</label>
        <select className="form-select w-100" disabled={isAntBlocked} value={estadoVacunacion} onChange={e => setEstadoVacunacion(e.target.value)}>
            <option value="">Seleccione...</option>
            <option>Completo para la edad</option>
            <option>Incompleto</option>
            <option>No vacunado</option>
        </select>
    </div>
);