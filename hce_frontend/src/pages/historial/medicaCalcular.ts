import { type Apgar } from "../historial/MedicalTypes";
export const calcularApgar = (a: Apgar) => a.apariencia + a.pulso + a.reflejos + a.tonoMuscular + a.respiracion;
export const validarFechaNoFutura = (fecha: string) => {
    const hoy = new Date().toISOString().split('T')[0];
    return fecha <= hoy;
};
export const calcularEdadMeses = (fechaNacimiento: string) => {
    if (!fechaNacimiento) return 0;
    const nac = new Date(fechaNacimiento);
    const hoy = new Date();
    let meses = (hoy.getFullYear() - nac.getFullYear()) * 12;
    meses += hoy.getMonth() - nac.getMonth();
    return meses <= 0 ? 0 : meses;
};

export const calcularIMC = (peso: number, tallaCm: number) => {
    if (!peso || !tallaCm || tallaCm <= 0) return { valor: "", categoria: "", color: "" };
    const tallaM = tallaCm / 100;
    const imc = peso / (tallaM * tallaM);
    const valor = imc.toFixed(2);
    let categoria = ""; let color = "";
    if (imc < 18.5) { categoria = "Bajo Peso"; color = "text-danger fw-bold"; }
    else if (imc < 25) { categoria = "Eutrófico"; color = "text-success fw-bold"; }
    else if (imc < 30) { categoria = "Sobrepeso"; color = "text-warning fw-bold"; }
    else { categoria = "Obesidad"; color = "text-danger fw-bold"; }
    return { valor, categoria, color };
};
export const obtenerZScore = (tipo: 'Peso/Edad' | 'Talla/Edad' | 'IMC/Edad', valor: number, edadMeses: number) => {
    if (!valor || edadMeses === undefined) return { valor: "", interpretacion: "", color: "" };
    if (edadMeses > 228) return { valor: "N/A", interpretacion: "Paciente Adulto", color: "text-secondary" };
    let media = 0; let ds = 1;
    if (tipo === 'Peso/Edad') {
        media = edadMeses < 12 ? 3.5 + (edadMeses * 0.6) : 10 + ((edadMeses - 12) * 0.2);
        ds = media * 0.12; 
    } else if (tipo === 'Talla/Edad') {
        media = 50 + (edadMeses * 1.5); ds = media * 0.04;
    } else if (tipo === 'IMC/Edad') {
        media = 16; ds = 1.5;
    }
    const z = (valor - media) / ds;
    const valorZ = (z > 0 ? "+" : "") + z.toFixed(2);
    let interpretacion = "Normal"; let color = "text-success fw-bold";
    if (z > 2 || z < -2) { color = "text-danger fw-bold"; interpretacion = z > 2 ? "Desviación Superior" : "Desviación Inferior"; }
    else if (z > 1 || z < -1) { color = "text-warning fw-bold"; interpretacion = "Riesgo / Límite"; }
    return { valor: `${valorZ} SD`, interpretacion, color };
};