package gt.edu.umg.desarrolloweb.consulta.repository;

/** Datos del contador, titular y ubicacion, leidos de una sola consulta. */
public record FichaContador(
        Long id,
        String codigo,
        String estado,
        String nombreTitular,
        String paja,
        String sector,
        String aldea,
        String zona,
        String calle,
        String numeroCasa) {
}
