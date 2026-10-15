package gt.edu.umg.desarrolloweb.consulta.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoFila(
        String folio,
        LocalDate fechaPago,
        BigDecimal monto,
        String metodo,
        String folioBoleta) {
}
