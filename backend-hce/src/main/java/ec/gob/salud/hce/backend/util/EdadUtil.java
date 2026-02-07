package ec.gob.salud.hce.backend.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class EdadUtil {

    private EdadUtil() {
        // Evita instanciación
    }

    public static Integer calcularEdad(LocalDate fechaNacimiento, LocalDateTime fechaReferencia) {

        if (fechaNacimiento == null || fechaReferencia == null) {
            return null;
        }

        LocalDate fechaRef = fechaReferencia.toLocalDate();

        if (fechaNacimiento.isAfter(fechaRef)) {
            return null;
        }

        return Period.between(fechaNacimiento, fechaRef).getYears();
    }
}
