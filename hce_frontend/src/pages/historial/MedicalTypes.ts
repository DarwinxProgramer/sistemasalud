export interface DiagnosticoItem {
  id: string; cie10: string; descripcion: string; tipo: 'Presuntivo' | 'Definitivo';
}
export interface ComplicacionExtra { id: string; cie10: string; descripcion: string; fecha: string; }
export interface EventoMedico { id: string; descripcion: string; fecha: string; } 
export interface Apgar {
  apariencia: number; pulso: number; reflejos: number; tonoMuscular: number; respiracion: number;
}