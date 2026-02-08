import { useState, useMemo, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';

// Importaciones de componentes seccionados
import { AlertaAlergia } from './components/alertaAlergia';
import { VistaIdentificacion } from './components/VistaIdentificacion';
import { SeccionAntecedentes } from './components/SeccionAntecedentes';
import { TabsConsultaActual } from './components/TabsConsultaActual';

// Servicios y utilidades
import { obtenerPacientes, agregarConsulta, actualizarConsultaExistente, dbHelpers } from "../../services/dbPacienteService";
import { calcularIMC, obtenerZScore, calcularEdadMeses } from './medicaCalcular';

export default function HistorialConsultas() {
    const { cedula } = useParams<{ cedula: string }>();
    const navigate = useNavigate();
    const location = useLocation();

    // 1. ESTADOS PRINCIPALES
    const [tabActiva, setTabActiva] = useState('anamnesis');
    const [pacienteActual, setPacienteActual] = useState<any>(null);
    const [bloquearAntecedentes, setBloquearAntecedentes] = useState(true);

    // 2. ESTADOS DE FORMULARIO
    const [motivoConsulta, setMotivoConsulta] = useState("");
    const [enfermedadActual, setEnfermedadActual] = useState("");

    // Antecedentes Perinatales
    const [productoGestacion, setProductoGestacion] = useState("");
    const [edadGestacional, setEdadGestacional] = useState<number | "">("");
    const [viaParto, setViaParto] = useState("");
    const [pesoNacimiento, setPesoNacimiento] = useState<number | "">("");
    const [tallaNacimiento, setTallaNacimiento] = useState<number | "">("");
    const [apgar, setApgar] = useState({ apariencia: 0, pulso: 0, reflejos: 0, tonoMuscular: 0, respiracion: 0 });
    const [checksComplicaciones, setChecksComplicaciones] = useState({ sdr: false, ictericia: false, sepsis: false, ninguna: true });
    const [descripcionComplicaciones, setDescripcionComplicaciones] = useState("");

    // Antecedentes Personales y Alergias
    const [enfermedadesCronicas, setEnfermedadesCronicas] = useState({ "Asma": false, "Diabetes": false, "Cardiopatías": false, "Epilepsia": false });
    const [hospitalizaciones, setHospitalizaciones] = useState({ tiene: false, descripcion: "", fecha: "" });
    const [cirugias, setCirugias] = useState({ tiene: false, descripcion: "", fecha: "" });
    const [alergias, setAlergias] = useState({ tiene: false, descripcion: "" });
    const [familiares, setFamiliares] = useState({ HTA: false, Diabetes: false, Cáncer: false, Genéticas: false, Ninguna: false, Otros: false });
    const [descripcionCronicas, setDescripcionCronicas] = useState("");

    // Inmunizaciones y Desarrollo
    const [estadoVacunacion, setEstadoVacunacion] = useState("");
    const [desarrollo, setDesarrollo] = useState({ sostenCefalico: "", sedestacion: "", deambulacion: "", lenguaje: "", desconoce: false });
    const [alimentacion, setAlimentacion] = useState({
        lactancia: { checked: false, duracion: "" },
        formula: { checked: false, tipo: "" },
        ablactacion: { checked: false, edadInicio: "" }
    });

    // Examen Físico y Diagnóstico
    const [signosVitales, setSignosVitales] = useState({ peso: "", talla: "", temperatura: "", fc: "", fr: "", spo2: "", paSistolica: "", paDiastolica: "", perimetroCefalico: "" });
    const [examenSegmentario, setExamenSegmentario] = useState({
        aspecto: { Consciente: true, Alerta: true, Activo: true, Decaído: false },
        piel: { Ictericia: false, Cianosis: false, Rash: false, Normal: true },
        cabeza: { 'Fontanela Anterior': true, Adenopatías: false, Normal: true },
        cardio: { 'Ruidos cardiacos': true, 'Murmullo vesicular': true, Soplos: false },
        abdomen: { Blando: true, Depresible: true, Hepatomegalia: false },
        neuro: { 'Reflejos': true, 'Tono': true }
    });
    const [evolucionClinica, setEvolucionClinica] = useState("");
    const [diagnosticoPrincipal, setDiagnosticoPrincipal] = useState({ id: uuidv4(), cie10: '', descripcion: '', tipo: 'Presuntivo' as 'Presuntivo' | 'Definitivo' });
    const [diagnosticosSecundarios, setDiagnosticosSecundarios] = useState<any[]>([]);
    const [estudiosSolicitados, setEstudiosSolicitados] = useState("");
    const [resultadosExamenes, setResultadosExamenes] = useState<any[]>([]);
    const [planFarmacologico, setPlanFarmacologico] = useState({ esquema: "", viaVenosa: "", viaOral: "" });
    const [planNoFarmacologico, setPlanNoFarmacologico] = useState({ hidratacion: false, dieta: false, oxigeno: false, fisio: false, otros: "" });
    const [pronostico, setPronostico] = useState("Bueno");
    const [proximaCita, setProximaCita] = useState("");

    // --- CARGA DE DATOS ---
    useEffect(() => {
        const cargarPaciente = async () => {
            const lista = await obtenerPacientes();
            const encontrado = lista.find((p: any) => String(p.cedula) === String(cedula));

            if (encontrado) {
                setPacienteActual(encontrado);
                // IMPORTANTE: Asegúrate de que 'historiaClinica' sea el nombre correcto del array en tu DB
                const historia = encontrado.historiaClinica || [];

                if (historia.length > 0) {
                    // Tomamos la última consulta registrada
                    const ult = historia[historia.length - 1];
                    const peri = ult.antecedentes?.perinatales;

                    if (peri) {
                        // Actualizamos los estados individuales que recibe el Tab
                        setProductoGestacion(peri.productoGestacion || "");
                        setEdadGestacional(peri.edadGestacional || "");
                        setViaParto(peri.viaParto || "");
                        setPesoNacimiento(peri.pesoNacimiento || "");
                        setTallaNacimiento(peri.tallaNacimiento || "");

                        // Si apgar existe, lo seteamos, si no, mantenemos el default
                        if (peri.apgar) setApgar(peri.apgar);

                        // Complicaciones
                        if (peri.checksComplicaciones) setChecksComplicaciones(peri.checksComplicaciones);
                        setDescripcionComplicaciones(peri.descripcionComplicaciones || "");
                    }

                    setBloquearAntecedentes(true);
                } else {
                    setBloquearAntecedentes(false);
                }
            }
        };
        cargarPaciente();

        const consultaEdicion = location.state?.consultaAEditar;
        if (consultaEdicion) {
            setMotivoConsulta(consultaEdicion.motivo || "");
            setEnfermedadActual(consultaEdicion.enfermedadActual || "");
            setSignosVitales(consultaEdicion.examenFisico?.vitales || {});
            setEvolucionClinica(consultaEdicion.examenFisico?.evolucion || "");
            if (consultaEdicion.diagnostico?.principal) setDiagnosticoPrincipal(consultaEdicion.diagnostico.principal);
            setPronostico(consultaEdicion.diagnostico?.pronostico || "Bueno");
            setProximaCita(consultaEdicion.diagnostico?.proximaCita || "");
        }
    }, [cedula, location.state]);

    // --- CÁLCULOS MÉDICOS ---
    const edadM = useMemo(() => pacienteActual ? calcularEdadMeses(pacienteActual.fechaNacimiento) : 0, [pacienteActual]);
    const resIMC = useMemo(() => {
        const p = parseFloat(signosVitales.peso); const t = parseFloat(signosVitales.talla);
        return (p && t) ? calcularIMC(p, t) : { valor: "0.00", interpretacion: "Pendiente", color: "#6c757d" };
    }, [signosVitales.peso, signosVitales.talla]);

    const zP = useMemo(() => obtenerZScore('Peso/Edad', parseFloat(signosVitales.peso), edadM), [signosVitales.peso, edadM]);
    const zT = useMemo(() => obtenerZScore('Talla/Edad', parseFloat(signosVitales.talla), edadM), [signosVitales.talla, edadM]);
    const zI = useMemo(() => obtenerZScore('IMC/Edad', parseFloat(resIMC.valor), edadM), [resIMC.valor, edadM]);

    // GUARDAR SOLO ANTECEDENTES (Primera consulta)
    const handleGuardarAntecedentes = async () => {
        console.log('[DEBUG] 🟦 Guardando solo antecedentes (primera consulta)...');

        try {
            if (!pacienteActual) {
                alert('Error: No se encontró el paciente');
                console.error('[ERROR] Paciente actual no existe');
                return;
            }

            // Actualizar solo los antecedentes del paciente
            const antecedentesActualizados = {
                perinatales: { productoGestacion, edadGestacional, viaParto, pesoNacimiento, tallaNacimiento, apgar, checksComplicaciones, descripcionComplicaciones },
                vacunacion: estadoVacunacion,
                personales: { enfermedadesCronicas, hospitalizaciones, cirugias, alergias, familiares, descripcionCronicas },
                desarrollo: { hitos: desarrollo, alimentacion }
            };

            console.log('[DEBUG] 📋 Antecedentes a guardar:', antecedentesActualizados);

            pacienteActual.antecedentes = antecedentesActualizados;

            // Importar dbHelpers dinámicamente para guardar
            // const { dbHelpers } = await import('../../services/dbPacienteService'); // Already imported statically
            await dbHelpers.savePaciente(pacienteActual);

            console.log('[DEBUG] ✅ Antecedentes guardados exitosamente');
            alert('Antecedentes guardados correctamente. Ahora puedes completar la consulta.');

        } catch (error) {
            console.error('[ERROR] ❌ Error guardando antecedentes:', error);
            alert('Error al guardar antecedentes. Ver consola para detalles.');
        }
    };

    // VALIDAR Y GUARDAR CONSULTA COMPLETA
    const handleGuardarConsulta = async () => {
        console.log('[DEBUG] 🟩 Iniciando validación y guardado de consulta...');

        try {
            // ========== VALIDACIONES ==========
            const errores: string[] = [];

            // 1. Validar Anamnesis
            console.log('[DEBUG] 🔍 Validando Anamnesis...');
            if (!motivoConsulta?.trim() && !enfermedadActual?.trim()) {
                errores.push('Anamnesis: Debe ingresar al menos el motivo de consulta o la enfermedad actual');
            }
            console.log('[DEBUG] Motivo:', motivoConsulta, '| Enfermedad:', enfermedadActual);

            // 2. Validar Examen Físico (al menos un signo vital)
            console.log('[DEBUG] 🔍 Validando Examen Físico...');
            const tieneSignosVitales = signosVitales.peso || signosVitales.talla ||
                signosVitales.temperatura || signosVitales.fc ||
                signosVitales.fr || signosVitales.spo2;

            if (!tieneSignosVitales) {
                errores.push('Examen Físico: Debe ingresar al menos un signo vital (peso, talla, temperatura, FC, FR o SpO2)');
            }
            console.log('[DEBUG] Signos Vitales:', signosVitales);

            // 3. Validar Diagnóstico (al menos descripción o plan)
            console.log('[DEBUG] 🔍 Validando Diagnóstico...');
            const tieneDiagnostico = diagnosticoPrincipal.descripcion?.trim() ||
                diagnosticoPrincipal.cie10?.trim();
            const tienePlan = planFarmacologico.esquema?.trim() ||
                planNoFarmacologico.otros?.trim() ||
                Object.values(planNoFarmacologico).some(v => v === true);

            if (!tieneDiagnostico && !tienePlan) {
                errores.push('Diagnóstico: Debe ingresar diagnóstico o plan terapéutico');
            }
            console.log('[DEBUG] Diagnóstico:', diagnosticoPrincipal, '| Plan:', { planFarmacologico, planNoFarmacologico });

            // Mostrar errores si existen
            if (errores.length > 0) {
                console.warn('[WARN] ⚠️ Errores de validación:', errores);
                alert('Por favor complete los siguientes campos:\n\n' + errores.join('\n'));
                return;
            }

            console.log('[DEBUG] ✅ Validaciones pasadas. Preparando consulta...');

            // ========== CONSTRUIR CONSULTA ==========
            const consultaCompleta = {
                id: location.state?.consultaAEditar?.id || uuidv4(),
                fecha: new Date().toLocaleDateString(),
                hora: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
                motivo: motivoConsulta,
                enfermedadActual,
                antecedentes: {
                    perinatales: { productoGestacion, edadGestacional, viaParto, pesoNacimiento, tallaNacimiento, apgar, checksComplicaciones, descripcionComplicaciones },
                    vacunacion: estadoVacunacion,
                    personales: { enfermedadesCronicas, hospitalizaciones, cirugias, alergias, familiares, descripcionCronicas },
                    desarrollo: { hitos: desarrollo, alimentacion }
                },
                examenFisico: { vitales: signosVitales, segmentario: examenSegmentario, evolucion: evolucionClinica, nutricion: { resIMC, zP, zT, zI } },
                diagnostico: { principal: diagnosticoPrincipal, secundarios: diagnosticosSecundarios, estudios: estudiosSolicitados, resultados: resultadosExamenes, plan: { farmacologico: planFarmacologico, noFarmacologico: planNoFarmacologico }, pronostico, proximaCita, impresion: enfermedadActual }
            };

            console.log('[DEBUG] 📝 Consulta completa construida:', consultaCompleta);

            // ========== GUARDAR CONSULTA ==========
            console.log('[DEBUG] 💾 Llamando a agregarConsulta...');
            let exito = location.state?.consultaAEditar
                ? await actualizarConsultaExistente(cedula!, consultaCompleta)
                : await agregarConsulta(cedula!, consultaCompleta);

            if (exito) {
                console.log('[DEBUG] ✅ Consulta guardada exitosamente');
                alert('✅ Consulta guardada exitosamente.');
                navigate('/pacientes/consulta');
            } else {
                console.error('[ERROR] ❌ agregarConsulta retornó false');
                alert('Error al guardar la consulta. Por favor revise la consola.');
            }

        } catch (error) {
            console.error('[ERROR] ❌ Excepción al guardar consulta:', error);
            console.error('[ERROR] Stack trace:', (error as Error).stack);
            alert('Error crítico al guardar consulta. Ver consola para detalles.');
        }
    };

    return (
        <div className="container-fluid p-0 bg-white" style={{ height: "100vh", overflowY: "auto" }}>
            <div className="bg-primary text-white p-3 shadow-sm d-flex justify-content-between align-items-center sticky-top" style={{ zIndex: 1040 }}>
                <h4 className="m-0 fw-bold"><i className="bi bi-file-earmark-medical me-2"></i>Nueva Historia Clínica</h4>
                <div className="d-flex gap-2">
                    <button className="btn btn-light btn-sm fw-bold" onClick={() => navigate(-1)}>CANCELAR</button>
                    <button className="btn btn-success btn-sm fw-bold shadow" onClick={handleGuardarAntecedentes}>GUARDAR TODO</button>
                </div>
            </div>

            <AlertaAlergia tiene={alergias.tiene} descripcion={alergias.descripcion} />

            <div className="p-4">
                <VistaIdentificacion cedula={cedula} paciente={pacienteActual} />

                <SeccionAntecedentes
                    bloquear={bloquearAntecedentes} setBloquear={setBloquearAntecedentes}
                    perinatales={{ productoGestacion, setProductoGestacion, edadGestacional, setEdadGestacional, viaParto, setViaParto, pesoNacimiento, setPesoNacimiento, tallaNacimiento, setTallaNacimiento, apgar, setApgar, checksComplicaciones, setChecksComplicaciones, descripcionComplicaciones, setDescripcionComplicaciones }}
                    personales={{ enfermedadesCronicas, setEnfermedadesCronicas, hospitalizaciones, setHospitalizaciones, cirugias, setCirugias, alergias, setAlergias, familiares, setFamiliares, descripcionCronicas, setDescripcionCronicas }}
                    inmunizaciones={{ estadoVacunacion, setEstadoVacunacion }}
                    desarrollo={{ desarrollo, setDesarrollo, alimentacion, setAlimentacion }}
                />

                <TabsConsultaActual
                    tabActiva={tabActiva} setTabActiva={setTabActiva}
                    anamnesis={{ motivoConsulta, setMotivoConsulta, enfermedadActual, setEnfermedadActual }}
                    fisico={{ signosVitales, setSignosVitales, examenSegmentario, setExamenSegmentario, zP, zT, zI, resIMC, evolucionClinica, setEvolucionClinica, pacienteActual }}
                    diagnostico={{ diagnosticoPrincipal, setDiagnosticoPrincipal, diagnosticosSecundarios, setDiagnosticosSecundarios, estudiosSolicitados, setEstudiosSolicitados, resultadosExamenes, setResultadosExamenes, planFarmacologico, setPlanFarmacologico, planNoFarmacologico, setPlanNoFarmacologico, pronostico, setPronostico, proximaCita, setProximaCita, handleGuardar: handleGuardarConsulta }}
                />
            </div>
        </div>
    );
}
