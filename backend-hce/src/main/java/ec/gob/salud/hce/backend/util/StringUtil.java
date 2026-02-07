package ec.gob.salud.hce.backend.util;

public final class StringUtil {

    private StringUtil() {
        // Evita instanciación
    }

    public static boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean tieneLongitudEntre(String texto, int min, int max) {
        if (texto == null) {
            return false;
        }
        int longitud = texto.trim().length();
        return longitud >= min && longitud <= max;
    }
}
