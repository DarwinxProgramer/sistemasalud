package ec.gob.salud.hce.backend.util;

import java.time.LocalDate;
import java.time.Period;

public final class FechaUtil {

    private FechaUtil() {
        // Evita instanciación
    }

    public static boolean esFechaNoNula(LocalDate fecha) {
        return fecha != null;
    }

    public static boolean esFechaNoFutura(LocalDate fecha) {
        return fecha != null && !fecha.isAfter(LocalDate.now());
    }

    public static boolean esFechaNacimientoValida(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        int edad = Period.between(fechaNacimiento, hoy).getYears();

        return !fechaNacimiento.isAfter(hoy) && edad >= 0 && edad <= 120;
    }

    public static boolean esMayorDeEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return false;
        }

        return Period.between(fechaNacimiento, LocalDate.now()).getYears() >= 18;
    }

    public static boolean estaDentroDeRango(LocalDate fecha,
                                           LocalDate inicio,
                                           LocalDate fin) {
        if (fecha == null || inicio == null || fin == null) {
            return false;
        }

        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }
}
