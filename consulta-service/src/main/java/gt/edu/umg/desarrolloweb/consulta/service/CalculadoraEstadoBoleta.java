package gt.edu.umg.desarrolloweb.consulta.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Regla de negocio: el monolito solo guarda {@code anulada_en}; el resto se deduce.
 * <ol>
 *   <li>Anulada si {@code anulada_en} no es nulo.</li>
 *   <li>Pagada si los pagos no revertidos cubren el monto.</li>
 *   <li>Vencida si no esta pagada y su fecha de vencimiento ya paso.</li>
 *   <li>Pendiente en cualquier otro caso (incluye el dia del vencimiento).</li>
 * </ol>
 */
public final class CalculadoraEstadoBoleta {

    private CalculadoraEstadoBoleta() {
    }

    public static EstadoBoleta calcular(boolean anulada, BigDecimal monto, BigDecimal pagado,
                                        LocalDate fechaVencimiento, LocalDate hoy) {
        if (anulada) {
            return EstadoBoleta.ANULADA;
        }
        if (pagado.compareTo(monto) >= 0) {
            return EstadoBoleta.PAGADA;
        }
        if (fechaVencimiento.isBefore(hoy)) {
            return EstadoBoleta.VENCIDA;
        }
        return EstadoBoleta.PENDIENTE;
    }
}
