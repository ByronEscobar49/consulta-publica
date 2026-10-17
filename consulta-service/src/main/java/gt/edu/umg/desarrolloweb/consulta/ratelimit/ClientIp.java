package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import java.util.regex.Pattern;

/**
 * Decide que IP se le cobra a cada consulta.
 *
 * <p>La web (BFF) es quien llama a este servicio, asi que {@code getRemoteAddr()} siempre seria
 * la IP del BFF. La IP real del visitante viaja en {@code X-Forwarded-For} (el primer valor).
 *
 * <p>ADVERTENCIA de seguridad: esa cabecera la puede escribir cualquiera. Solo debe confiarse
 * en ella si este servicio es alcanzable UNICAMENTE a traves del BFF (red interna de Docker);
 * si estuviera expuesto a internet, un atacante cambiaria la cabecera en cada peticion y se
 * saltaria el limite. Por eso la confianza es configurable.
 */
public final class ClientIp {

    /** IPv4 o IPv6 sin espacios ni saltos de linea (tambien evita inyeccion en los logs). */
    private static final Pattern IP_VALIDA = Pattern.compile("^[0-9a-fA-F:.]{3,45}$");

    private ClientIp() {
    }

    public static String resolve(String forwardedFor, String remoteAddr, boolean trustForwardedFor) {
        if (trustForwardedFor && forwardedFor != null && !forwardedFor.isBlank()) {
            String primera = forwardedFor.split(",")[0].trim();
            if (IP_VALIDA.matcher(primera).matches()) {
                return primera.toLowerCase();
            }
        }
        return remoteAddr == null ? "desconocida" : remoteAddr;
    }
}
