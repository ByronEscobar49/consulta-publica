package gt.edu.umg.desarrolloweb.consulta.repository;

import java.math.BigDecimal;

/** Suma de pagos NO revertidos de una boleta. */
public record PagadoPorBoleta(Long boletaId, BigDecimal total) {
}
