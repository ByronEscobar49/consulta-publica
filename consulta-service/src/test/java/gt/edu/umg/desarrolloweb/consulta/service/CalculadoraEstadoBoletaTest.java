package gt.edu.umg.desarrolloweb.consulta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CalculadoraEstadoBoletaTest {

    private static final LocalDate HOY = LocalDate.of(2026, 10, 14);
    private static final BigDecimal MONTO = new BigDecimal("60.00");

    @Test
    void anuladaGanaSobreTodo() {
        assertEquals(EstadoBoleta.ANULADA,
                CalculadoraEstadoBoleta.calcular(true, MONTO, MONTO, HOY.minusDays(30), HOY));
    }

    @Test
    void pagadaCuandoLosPagosCubrenElMonto() {
        assertEquals(EstadoBoleta.PAGADA,
                CalculadoraEstadoBoleta.calcular(false, MONTO, new BigDecimal("60.00"), HOY.minusDays(30), HOY));
    }

    @Test
    void pagoParcialVencidoSigueVencida() {
        assertEquals(EstadoBoleta.VENCIDA,
                CalculadoraEstadoBoleta.calcular(false, new BigDecimal("80.00"), new BigDecimal("40.00"), HOY.minusDays(1), HOY));
    }

    @Test
    void sinPagarYVencidaEsVencida() {
        assertEquals(EstadoBoleta.VENCIDA,
                CalculadoraEstadoBoleta.calcular(false, MONTO, BigDecimal.ZERO, HOY.minusDays(1), HOY));
    }

    @Test
    void elDiaDelVencimientoAunEsPendiente() {
        assertEquals(EstadoBoleta.PENDIENTE,
                CalculadoraEstadoBoleta.calcular(false, MONTO, BigDecimal.ZERO, HOY, HOY));
    }

    @Test
    void sinPagarYAunSinVencerEsPendiente() {
        assertEquals(EstadoBoleta.PENDIENTE,
                CalculadoraEstadoBoleta.calcular(false, MONTO, BigDecimal.ZERO, HOY.plusDays(2), HOY));
    }
}
