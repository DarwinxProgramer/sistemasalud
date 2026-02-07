export type Apgar = {
  apariencia: 0 | 1 | 2
  pulso: 0 | 1 | 2
  reflejos: 0 | 1 | 2
  tonoMuscular: 0 | 1 | 2
  respiracion: 0 | 1 | 2
}

export const calcularApgar = (apgar: Apgar): number => {
  return (
    apgar.apariencia +
    apgar.pulso +
    apgar.reflejos +
    apgar.tonoMuscular +
    apgar.respiracion
  )
}
