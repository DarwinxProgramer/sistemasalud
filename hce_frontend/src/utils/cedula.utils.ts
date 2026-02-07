export const validarCedulaEcuatoriana = (cedula: string): boolean => {
  if (!/^\d{10}$/.test(cedula)) return false

  const provincia = Number(cedula.substring(0, 2))
  if (provincia < 1 || provincia > 24) return false

  const digitos = cedula.split('').map(Number)
  const verificador = digitos.pop()!

  let suma = 0
  digitos.forEach((d, i) => {
    let valor = i % 2 === 0 ? d * 2 : d
    if (valor > 9) valor -= 9
    suma += valor
  })

  const resultado = (10 - (suma % 10)) % 10
  return resultado === verificador
}
