package gt.edu.umg.desarrolloweb.consulta.service;

import gt.edu.umg.desarrolloweb.consulta.repository.FichaContador;
import java.util.ArrayList;
import java.util.List;

/** Arma los textos que se muestran al publico sin revelar datos personales. */
public final class TextoPublico {

    private TextoPublico() {
    }

    /** "Maria Fernanda Lopez Castillo" -> "Maria F. L. C." (primer nombre completo, el resto en iniciales). */
    public static String titularAbreviado(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            return "";
        }
        String[] partes = nombreCompleto.trim().split("\\s+");
        StringBuilder sb = new StringBuilder(partes[0]);
        for (int i = 1; i < partes.length; i++) {
            sb.append(' ').append(Character.toUpperCase(partes[i].charAt(0))).append('.');
        }
        return sb.toString();
    }

    /** Ubicacion del SERVICIO (no la direccion de notificacion del titular). */
    public static String ubicacion(FichaContador f) {
        List<String> partes = new ArrayList<>();
        agregar(partes, f.sector());
        if (!esVacio(f.aldea()) && (esVacio(f.sector()) || !f.sector().contains(f.aldea()))) {
            partes.add(f.aldea().trim());
        }
        if (!esVacio(f.zona())) {
            partes.add("zona " + f.zona().trim());
        }
        if (!esVacio(f.calle())) {
            partes.add(f.calle().trim() + (esVacio(f.numeroCasa()) ? "" : " " + f.numeroCasa().trim()));
        } else if (!esVacio(f.numeroCasa())) {
            partes.add("casa " + f.numeroCasa().trim());
        }
        return String.join(", ", partes);
    }

    private static void agregar(List<String> partes, String valor) {
        if (!esVacio(valor)) {
            partes.add(valor.trim());
        }
    }

    private static boolean esVacio(String s) {
        return s == null || s.isBlank();
    }
}
