package gt.edu.umg.desarrolloweb.consulta.service;

import gt.edu.umg.desarrolloweb.consulta.api.dto.BoletaDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.ContadorDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.EstadoDeCuentaDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.PagoDto;
import gt.edu.umg.desarrolloweb.consulta.api.dto.ResumenDto;
import gt.edu.umg.desarrolloweb.consulta.repository.BoletaFila;
import gt.edu.umg.desarrolloweb.consulta.repository.BoletaRepository;
import gt.edu.umg.desarrolloweb.consulta.repository.ContadorRepository;
import gt.edu.umg.desarrolloweb.consulta.repository.FichaContador;
import gt.edu.umg.desarrolloweb.consulta.repository.PagadoPorBoleta;
import gt.edu.umg.desarrolloweb.consulta.repository.PagoRepository;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Arma el estado de cuenta publico de un contador. Solo lee; readOnly lo declara. */
@Service
@Transactional(readOnly = true)
public class ConsultaService {

    /** Maximo de boletas y de pagos que se devuelven (coincide con maxItems del contrato). */
    static final int MAX_ITEMS = 12;

    private final ContadorRepository contadorRepository;
    private final BoletaRepository boletaRepository;
    private final PagoRepository pagoRepository;
    private final Clock clock;

    public ConsultaService(ContadorRepository contadorRepository, BoletaRepository boletaRepository,
                           PagoRepository pagoRepository, Clock clock) {
        this.contadorRepository = contadorRepository;
        this.boletaRepository = boletaRepository;
        this.pagoRepository = pagoRepository;
        this.clock = clock;
    }

    public EstadoDeCuentaDto consultar(String codigoContador) {
        FichaContador ficha = contadorRepository.buscarFicha(codigoContador.trim())
                .orElseThrow(ContadorNoEncontradoException::new);

        LocalDate hoy = LocalDate.now(clock);
        List<BoletaFila> filas = boletaRepository.boletasDelContador(ficha.id());

        Map<Long, BigDecimal> pagado = filas.isEmpty()
                ? Map.of()
                : pagoRepository.pagadoPorBoleta(filas.stream().map(BoletaFila::id).toList()).stream()
                        .collect(Collectors.toMap(PagadoPorBoleta::boletaId, PagadoPorBoleta::total));

        // Se calculan TODAS las boletas (el saldo debe incluir las viejas); solo se muestran las ultimas 12.
        List<BoletaDto> boletas = new ArrayList<>();
        BigDecimal saldoPendiente = BigDecimal.ZERO;
        int pendientes = 0;
        int vencidas = 0;
        for (BoletaFila fila : filas) {
            BigDecimal montoPagado = pagado.getOrDefault(fila.id(), BigDecimal.ZERO);
            EstadoBoleta estado = CalculadoraEstadoBoleta.calcular(
                    fila.anuladaEn() != null, fila.monto(), montoPagado, fila.fechaVencimiento(), hoy);
            BigDecimal saldo = (estado == EstadoBoleta.ANULADA || estado == EstadoBoleta.PAGADA)
                    ? BigDecimal.ZERO
                    : fila.monto().subtract(montoPagado).max(BigDecimal.ZERO);

            if (estado == EstadoBoleta.PENDIENTE) {
                pendientes++;
            } else if (estado == EstadoBoleta.VENCIDA) {
                vencidas++;
            }
            saldoPendiente = saldoPendiente.add(saldo);

            if (boletas.size() < MAX_ITEMS) {
                boletas.add(aBoletaDto(fila, montoPagado, saldo, estado));
            }
        }

        ResumenDto resumen = new ResumenDto();
        resumen.setSaldoPendiente(saldoPendiente);
        resumen.setBoletasPendientes(pendientes);
        resumen.setBoletasVencidas(vencidas);
        if (!filas.isEmpty()) {
            resumen.setUltimoPeriodo(periodo(filas.get(0)));
        }

        List<PagoDto> pagos = pagoRepository.ultimosPagos(ficha.id(), PageRequest.of(0, MAX_ITEMS)).stream()
                .map(p -> {
                    PagoDto dto = new PagoDto();
                    dto.setFolio(p.folio());
                    dto.setFechaPago(p.fechaPago());
                    dto.setMonto(p.monto());
                    dto.setMetodo(p.metodo());
                    dto.setFolioBoleta(p.folioBoleta());
                    return dto;
                })
                .toList();

        ContadorDto contador = new ContadorDto();
        contador.setCodigo(ficha.codigo());
        contador.setEstado(estadoContador(ficha.estado()));
        contador.setPaja(ficha.paja());
        contador.setUbicacion(TextoPublico.ubicacion(ficha));

        EstadoDeCuentaDto respuesta = new EstadoDeCuentaDto();
        respuesta.setContador(contador);
        respuesta.setTitular(TextoPublico.titularAbreviado(ficha.nombreTitular()));
        respuesta.setResumen(resumen);
        respuesta.setBoletas(boletas);
        respuesta.setPagos(pagos);
        respuesta.setConsultadoEn(clock.instant().atOffset(ZoneOffset.UTC));
        return respuesta;
    }

    private static BoletaDto aBoletaDto(BoletaFila fila, BigDecimal montoPagado, BigDecimal saldo, EstadoBoleta estado) {
        BoletaDto dto = new BoletaDto();
        dto.setFolio(fila.folio());
        dto.setPeriodo(periodo(fila));
        dto.setConsumoM3(fila.consumoM3());
        dto.setMonto(fila.monto());
        dto.setMontoPagado(montoPagado);
        dto.setSaldo(saldo);
        dto.setFechaEmision(fila.fechaEmision());
        dto.setFechaVencimiento(fila.fechaVencimiento());
        dto.setEstado(BoletaDto.EstadoEnum.valueOf(estado.name()));
        return dto;
    }

    private static String periodo(BoletaFila fila) {
        return String.format("%04d-%02d", fila.anio(), fila.mes());
    }

    /** La BD guarda activo / inactivo / "dañado"; se normaliza al enum del contrato sin depender de la enie. */
    private static ContadorDto.EstadoEnum estadoContador(String estadoBd) {
        String v = estadoBd == null ? "" : estadoBd.toLowerCase(Locale.ROOT);
        if (v.startsWith("inact")) {
            return ContadorDto.EstadoEnum.INACTIVO;
        }
        if (v.startsWith("da")) {
            return ContadorDto.EstadoEnum.DANADO;
        }
        return ContadorDto.EstadoEnum.ACTIVO;
    }
}
