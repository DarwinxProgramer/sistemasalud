export function calcularEdad(fechaNacimiento: string) {
  const hoy = new Date()
  const nacimiento = new Date(fechaNacimiento)

  if (nacimiento > hoy) return null

  let años = hoy.getFullYear() - nacimiento.getFullYear()
  let meses = hoy.getMonth() - nacimiento.getMonth()

  if (meses < 0) {
    años--
    meses += 12
  }

  return { años, meses }
}
