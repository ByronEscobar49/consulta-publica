package gt.edu.umg.desarrolloweb.consulta.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Una boleta con su periodo. Los tipos (Short, Byte) deben coincidir con los de las entidades. */
public record BoletaFila(
        Long id,
        String folio,
        Short anio,
        Byte mes,
        BigDecimal consumoM3,
        BigDecimal monto,
        LocalDate fechaEmision,
        LocalDate fechaVencimiento,
        LocalDateTime anuladaEn) {
}
