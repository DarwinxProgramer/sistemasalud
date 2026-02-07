import { useState, useEffect } from 'react'; // Se agrega useEffect
import { registrarPaciente } from "../../services/dbPacienteService";
import { v4 as uuidv4 } from "uuid";
import { useToast } from "../../contexts/ToastContext";
import { dbHelpers } from "../../db/db";
import { syncService } from "../../services/syncService";

export default function RegistroPaciente() {
  const [activeTab, setActiveTab] = useState<'identificacion' | 'filiacion'>('identificacion');
  const { showSuccessToast, showWarningToast, showToast } = useToast();
  const showInfoToast = (msg: string) => showToast(msg, 'info');

  // MODIFICACIÓN 1: El nombre se inicializa vacío y se carga del login
  const [nombreDoctor, setNombreDoctor] = useState("Cargando...");
  const [inicial, setInicial] = useState('D');

  useEffect(() => {
    const storedUser = localStorage.getItem('usuarioLogueado');
    if (storedUser) {
      const userData = JSON.parse(storedUser);
      setNombreDoctor(userData.nombre);
      const letra = userData.nombre.split(" ")[1]?.charAt(0).toUpperCase() || "D";
      setInicial(letra);
    }
  }, []);

  // ================= FUNCIÓN AUXILIAR: FECHA DE HOY =================
  const obtenerFechaHoy = () => {
    const hoy = new Date();
    const year = hoy.getFullYear();
    const month = String(hoy.getMonth() + 1).padStart(2, '0');
    const day = String(hoy.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  // ================= FUNCIÓN AUXILIAR: VALIDAR CÉDULA ECUATORIANA =================
  const validarCedulaEcuatoriana = (cedula: string) => {
    if (cedula.length !== 10) return false;
    const digits = cedula.split('').map(Number);
    if (digits.some(isNaN)) return false;
    const provincia = Number(cedula.substring(0, 2));
    if (provincia < 1 || provincia > 24) return false;
    const digitoTercero = digits[2];
    if (digitoTercero >= 6) return false;
    const coeficientes = [2, 1, 2, 1, 2, 1, 2, 1, 2];
    let suma = 0;
    for (let i = 0; i < 9; i++) {
      let valor = digits[i] * coeficientes[i];
      if (valor >= 10) valor -= 9;
      suma += valor;
    }
    const digitoVerificadorCalculado = suma % 10 === 0 ? 0 : 10 - (suma % 10);
    return digitoVerificadorCalculado === digits[9];
  };

  // ================= 1. DATOS DE IDENTIFICACIÓN DEL PACIENTE =================
  const [cedula, setCedula] = useState('');
  const [primerNombre, setPrimerNombre] = useState('');
  const [segundoNombre, setSegundoNombre] = useState('');
  const [primerApellido, setPrimerApellido] = useState('');
  const [segundoApellido, setSegundoApellido] = useState('');
  const [fechaCreacion, setFechaCreacion] = useState(obtenerFechaHoy());
  const [fechaNacimiento, setFechaNacimiento] = useState('');
  const [edad, setEdad] = useState<{ años: number; meses: number } | null>(null);
  const [mensajeEdad, setMensajeEdad] = useState('');
  const [sexo, setSexo] = useState('');
  const [grupoEtnico, setGrupoEtnico] = useState('');
  const [provincia, setProvincia] = useState('');
  const [canton, setCanton] = useState('');
  const [parroquia, setParroquia] = useState('');
  const [tipoSangre, setTipoSangre] = useState('');

  // ================= 2. DATOS DE FILIACIÓN (RESPONSABLE) =================
  const [primerNombreRes, setPrimerNombreRes] = useState('');
  const [segundoNombreRes, setSegundoNombreRes] = useState('');
  const [primerApellidoRes, setPrimerApellidoRes] = useState('');
  const [segundoApellidoRes, setSegundoApellidoRes] = useState('');
  const [provinciaRes, setProvinciaRes] = useState('');
  const [cantonRes, setCantonRes] = useState('');
  const [parroquiaRes, setParroquiaRes] = useState('');
  const [parentesco, setParentesco] = useState('');
  const [telefonoContacto, setTelefonoContacto] = useState('');
  const [domicilioActual, setDomicilioActual] = useState('');
  const [nivelEducativoResponsable, setNivelEducativoResponsable] = useState('');

  // LÓGICA DE EDAD
  const manejarFechaNacimiento = (fecha: string) => {
    setFechaNacimiento(fecha);
    setMensajeEdad('');
    if (!fecha) { setEdad(null); return; }
    const nacimiento = new Date(fecha);
    const hoy = new Date();
    if (nacimiento.getTime() > hoy.getTime()) {
      setEdad(null);
      setMensajeEdad("La fecha de nacimiento no puede ser mayor a la actual");
      return;
    }
    let anos = hoy.getFullYear() - nacimiento.getFullYear();
    let meses = hoy.getMonth() - nacimiento.getMonth();
    if (meses < 0 || (meses === 0 && hoy.getDate() < nacimiento.getDate())) { anos--; meses += 12; }
    if (hoy.getDate() < nacimiento.getDate()) { meses--; }
    if (meses < 0) { meses += 12; }
    setEdad({ años: anos, meses: meses });
  };

  // LISTAS
  const provincias = ["Azuay", "Pichincha", "Guayas", "Manabí", "Loja", "Imbabura", "Cotopaxi"];
  const cantonesPorProvincia: Record<string, string[]> = {
    "Azuay": ["Cuenca", "Gualaceo", "Paute"],
    "Pichincha": ["Quito", "Cayambe", "Rumiñahui"],
    "Guayas": ["Guayaquil", "Daule", "Samborondón"]
  };
  const parroquiasPorCanton: Record<string, string[]> = {
    "Cuenca": ["El Vecino", "Tarqui", "Baños", "Totoracocha"],
    "Quito": ["Centro Histórico", "Carcelén", "Iñaquito"],
    "Guayaquil": ["Tarqui", "Ximena", "Kennedy"]
  };
  const parentescos = ["Madre", "Padre", "Abuelo/a", "Tío/a", "Hermano/a", "Otro/Tutor"];
  const nivelesEducativos = ["Ninguno", "Primaria", "Secundaria", "Superior / Universitario", "Postgrado"];

  // VALIDACIÓN
  const [errores, setErrores] = useState<Record<string, string>>({});
  const validarFormulario = () => {
    const e: any = {};

    // Validación de Identificación
    if (!cedula.trim()) e.cedula = 'Requerido';
    else if (!validarCedulaEcuatoriana(cedula)) e.cedula = 'Cédula inválida';
    if (!primerNombre.trim()) e.primerNombre = 'Requerido';
    if (!primerApellido.trim()) e.primerApellido = 'Requerido';
    if (!fechaNacimiento) e.fechaNacimiento = 'Requerido';
    if (mensajeEdad) e.edad = mensajeEdad;
    if (!sexo) e.sexo = 'Requerido';
    if (!provincia) e.provincia = 'Requerido';
    if (!canton) e.canton = 'Requerido';

    // Validación de Filiación y Contacto
    if (!primerNombreRes.trim()) e.primerNombreRes = 'Requerido';
    if (!primerApellidoRes.trim()) e.primerApellidoRes = 'Requerido';
    if (!parentesco) e.parentesco = 'Requerido';
    if (!telefonoContacto.trim()) e.telefonoContacto = 'Requerido';
    else if (telefonoContacto.length !== 10) e.telefonoContacto = 'Debe tener 10 dígitos';

    setErrores(e);
    return Object.keys(e).length === 0;
  };

  const [guardando, setGuardando] = useState(false);

  const handleGuardarPaciente = async () => {
    if (guardando) return; // Prevenir doble clic

    if (!validarFormulario()) {
      // Determinar en qué pestaña están los errores
      const tieneErroresIdentificacion = errores.cedula || errores.primerNombre || errores.primerApellido || errores.fechaNacimiento || errores.sexo || errores.provincia || errores.canton;
      const tieneErroresFiliacion = errores.primerNombreRes || errores.primerApellidoRes || errores.parentesco || errores.telefonoContacto;

      if (tieneErroresIdentificacion && activeTab !== 'identificacion') {
        setActiveTab('identificacion');
        alert("Por favor complete todos los campos obligatorios en la pestaña 'Datos de Identificación'.");
      } else if (tieneErroresFiliacion && activeTab !== 'filiacion') {
        setActiveTab('filiacion');
        alert("Por favor complete todos los campos obligatorios en la pestaña 'Filiación y Contacto'.");
      } else if (errores.cedula === 'Cédula inválida') {
        alert("Error: Cédula del paciente inválida.");
      } else if (mensajeEdad) {
        alert("Error: " + mensajeEdad);
      } else {
        alert("Por favor complete todos los campos obligatorios marcados con *");
      }
      return;
    }

    setGuardando(true);

    const nuevoId = uuidv4();
    const nombresPaciente = `${primerNombre.trim()} ${segundoNombre.trim()}`.trim();
    const apellidosPaciente = `${primerApellido.trim()} ${segundoApellido.trim()}`.trim();
    const nombresResponsable = `${primerNombreRes.trim()} ${segundoNombreRes.trim()}`.trim();
    const apellidosResponsable = `${primerApellidoRes.trim()} ${segundoApellidoRes.trim()}`.trim();
    const nombreCompletoResponsable = `${nombresResponsable} ${apellidosResponsable}`.trim();

    const paciente = {
      id: nuevoId,
      cedula, nombres: nombresPaciente, apellidos: apellidosPaciente,
      fechaCreacion, fechaNacimiento,
      edad: edad ? `${edad.años} años, ${edad.meses} meses` : '',
      sexo, grupoEtnico, provincia, canton, parroquia, tipoSangre,
      filiacion: {
        nombreResponsable: nombreCompletoResponsable,
        parentesco, telefonoContacto, nivelEducativoResponsable,
        domicilioActual, provincia: provinciaRes, canton: cantonRes, parroquia: parroquiaRes
      },
      historiaClinica: []
    };
    try {
      await registrarPaciente(paciente as any);

      // Mostrar notificación con contador de pacientes pendientes
      const pendingItems = await dbHelpers.getPendingSyncItems();
      if (!navigator.onLine) {
        showWarningToast(`✅ Paciente guardado offline - ${pendingItems.length} paciente(s) pendiente(s) de sincronizar`);
      } else {
        showSuccessToast('✅ Paciente registrado correctamente');
      }

    } catch (err: any) {
      alert(err.message);
    } finally {
      setGuardando(false);
    }
  };

  return (
    <>
      <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
        <h4 className="text-primary m-0"><i className="bi bi-person-lines-fill me-2"></i> Registro de Historia Clínica Pediátrica</h4>
        <div className="d-flex align-items-center bg-white px-3 py-2 rounded shadow-sm border">
          <div className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-2 fw-bold" style={{ width: '35px', height: '35px' }}>
            {inicial}
          </div>
          <div className="lh-1">
            <small className="d-block text-muted" style={{ fontSize: '0.65rem' }}>Profesional</small>
            <span className="fw-bold text-dark small">{nombreDoctor}</span>
          </div>
        </div>
      </div>

      <ul className="nav nav-tabs mb-4">
        <li className="nav-item"><button className={`nav-link ${activeTab === 'identificacion' ? 'active fw-bold' : ''}`} onClick={() => setActiveTab('identificacion')}>1. Datos de Identificación</button></li>
        <li className="nav-item"><button className={`nav-link ${activeTab === 'filiacion' ? 'active fw-bold' : ''}`} onClick={() => setActiveTab('filiacion')}>2. Filiación y Contacto</button></li>
      </ul>

      <div className="card shadow-sm border-0">
        <div className="card-body p-4">
          {activeTab === 'identificacion' && (
            <div className="row g-3">
              <div className="col-12"><h6 className="text-muted border-bottom pb-2">Datos Personales del Paciente</h6></div>
              <div className="col-md-12">
                <label className="form-label fw-bold small text-primary">NÚMERO DE CÉDULA</label>
                <input type="text" maxLength={10} className={`form-control ${errores.cedula ? 'is-invalid' : ''}`} value={cedula} onChange={e => { if (/^\d*$/.test(e.target.value)) setCedula(e.target.value); }} placeholder="Ingrese los 10 dígitos" />
                {errores.cedula && <div className="invalid-feedback">{errores.cedula}</div>}
              </div>
              <div className="col-md-3"><label className="form-label fw-bold small">PRIMER NOMBRE <span className="text-danger">*</span></label><input className={`form-control ${errores.primerNombre ? 'is-invalid' : ''}`} value={primerNombre} onChange={e => setPrimerNombre(e.target.value)} /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">SEGUNDO NOMBRE</label><input className="form-control" value={segundoNombre} onChange={e => setSegundoNombre(e.target.value)} placeholder="(Opcional)" /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">PRIMER APELLIDO <span className="text-danger">*</span></label><input className={`form-control ${errores.primerApellido ? 'is-invalid' : ''}`} value={primerApellido} onChange={e => setPrimerApellido(e.target.value)} /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">SEGUNDO APELLIDO</label><input className="form-control" value={segundoApellido} onChange={e => setSegundoApellido(e.target.value)} placeholder="(Opcional)" /></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Fecha de Creación</label><input type="date" className={`form-control ${errores.fechaCreacion ? 'is-invalid' : ''}`} value={fechaCreacion} onChange={e => setFechaCreacion(e.target.value)} /></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Fecha de Nacimiento</label><input type="date" className={`form-control ${errores.fechaNacimiento ? 'is-invalid' : ''}`} value={fechaNacimiento} onChange={e => manejarFechaNacimiento(e.target.value)} /></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Edad</label><input className={`form-control ${mensajeEdad ? 'is-invalid' : 'bg-light'}`} disabled value={mensajeEdad ? mensajeEdad : (edad ? `${edad.años} años, ${edad.meses} meses` : 'Seleccione fecha...')} style={{ fontWeight: 'bold', color: mensajeEdad ? '#dc3545' : '#0d6efd', fontSize: mensajeEdad ? '0.8rem' : '1rem' }} /></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Sexo</label><select className="form-select" value={sexo} onChange={e => setSexo(e.target.value)}><option value="">Seleccione...</option><option>Masculino</option><option>Femenino</option></select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Tipo de Sangre</label><select className="form-select" value={tipoSangre} onChange={e => setTipoSangre(e.target.value)}><option value="">Seleccione...</option><option>O+</option><option>O-</option><option>A+</option><option>A-</option></select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Grupo Étnico</label><select className="form-select" value={grupoEtnico} onChange={e => setGrupoEtnico(e.target.value)}><option value="">Seleccione...</option><option>Mestizo</option><option>Blanco</option><option>Indígena</option><option>Afroecuatoriano</option></select></div>
              <div className="col-12 mt-4"><h6 className="text-muted border-bottom pb-2">Ubicación Geográfica del Paciente</h6></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Provincia</label><select className="form-select" value={provincia} onChange={e => { setProvincia(e.target.value); setCanton(''); setParroquia('') }}><option value="">Seleccione...</option>{provincias.map(p => <option key={p}>{p}</option>)}</select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Cantón</label><select className="form-select" value={canton} onChange={e => { setCanton(e.target.value); setParroquia('') }} disabled={!provincia}><option value="">Seleccione...</option>{provincia && cantonesPorProvincia[provincia]?.map(c => <option key={c}>{c}</option>)}</select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Parroquia</label><select className="form-select" value={parroquia} onChange={e => setParroquia(e.target.value)} disabled={!canton}><option value="">Seleccione...</option>{canton && parroquiasPorCanton[canton]?.map(p => <option key={p}>{p}</option>)}</select></div>
            </div>
          )}
          {activeTab === 'filiacion' && (
            <div className="row g-3">
              <div className="col-12"><h6 className="text-muted border-bottom pb-2">Datos del Responsable / Tutor</h6></div>
              <div className="col-md-3"><label className="form-label fw-bold small">PRIMER NOMBRE <span className="text-danger">*</span></label><input className={`form-control ${errores.primerNombreRes ? 'is-invalid' : ''}`} value={primerNombreRes} onChange={e => setPrimerNombreRes(e.target.value)} placeholder="Ej: María" /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">SEGUNDO NOMBRE</label><input className="form-control" value={segundoNombreRes} onChange={e => setSegundoNombreRes(e.target.value)} placeholder="(Opcional)" /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">PRIMER APELLIDO <span className="text-danger">*</span></label><input className={`form-control ${errores.primerApellidoRes ? 'is-invalid' : ''}`} value={primerApellidoRes} onChange={e => setPrimerApellidoRes(e.target.value)} placeholder="Ej: Lopez" /></div>
              <div className="col-md-3"><label className="form-label fw-bold small">SEGUNDO APELLIDO</label><input className="form-control" value={segundoApellidoRes} onChange={e => setSegundoApellidoRes(e.target.value)} placeholder="(Opcional)" /></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Parentesco con el Paciente <span className="text-danger">*</span></label><select className={`form-select ${errores.parentesco ? 'is-invalid' : ''}`} value={parentesco} onChange={e => setParentesco(e.target.value)}><option value="">Seleccione...</option>{parentescos.map(p => <option key={p}>{p}</option>)}</select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Teléfono (Celular) <span className="text-danger">*</span></label><input type="text" maxLength={10} className={`form-control ${errores.telefonoContacto ? 'is-invalid' : ''}`} value={telefonoContacto} onChange={e => { if (/^\d*$/.test(e.target.value)) setTelefonoContacto(e.target.value); }} placeholder="09XXXXXXXX" /><div className="form-text small">Máximo 10 dígitos.</div></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Nivel Educativo</label><select className="form-select" value={nivelEducativoResponsable} onChange={e => setNivelEducativoResponsable(e.target.value)}><option value="">Seleccione...</option>{nivelesEducativos.map(n => <option key={n}>{n}</option>)}</select></div>
              <div className="col-12 mt-3"><h6 className="text-muted border-bottom pb-2">Ubicación del Responsable</h6></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Provincia</label><select className="form-select" value={provinciaRes} onChange={e => { setProvinciaRes(e.target.value); setCantonRes(''); setParroquiaRes('') }}><option value="">Seleccione...</option>{provincias.map(p => <option key={p}>{p}</option>)}</select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Cantón</label><select className="form-select" value={cantonRes} onChange={e => { setCantonRes(e.target.value); setParroquiaRes('') }} disabled={!provinciaRes}><option value="">Seleccione...</option>{provinciaRes && cantonesPorProvincia[provinciaRes]?.map(c => <option key={c}>{c}</option>)}</select></div>
              <div className="col-md-4"><label className="form-label fw-bold small">Parroquia</label><select className="form-select" value={parroquiaRes} onChange={e => setParroquiaRes(e.target.value)} disabled={!cantonRes}><option value="">Seleccione...</option>{cantonRes && parroquiasPorCanton[cantonRes]?.map(p => <option key={p}>{p}</option>)}</select></div>
              <div className="col-12"><label className="form-label fw-bold small">Dirección Domiciliaria (Calle y Nro)</label><textarea className="form-control" rows={2} value={domicilioActual} onChange={e => setDomicilioActual(e.target.value)} placeholder="Ej: Calle Larga y Benigno Malo..."></textarea></div>
            </div>
          )}
        </div>
        <div className="card-footer bg-white text-end py-3">
          {/* MODIFICACIÓN 2: Cambio de btn-success a btn-primary */}
          <button className="btn btn-primary px-4 fw-bold" onClick={handleGuardarPaciente}><i className="bi bi-save me-2"></i> GUARDAR PACIENTE</button>
        </div>
      </div>
    </>
  );
}
