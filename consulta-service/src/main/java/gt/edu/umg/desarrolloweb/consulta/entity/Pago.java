package gt.edu.umg.desarrolloweb.consulta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.Immutable;

/**
 * Mapeo de SOLO LECTURA de la tabla {@code pagos} del monolito.
 * Se mapean unicamente las columnas que la consulta publica necesita: lo que no se mapea
 * (NIT, DPI, telefono, email...) no puede filtrarse por accidente. Se usa solo en consultas JPQL.
 */
@Entity
@Immutable
@Table(name = "pagos")
@SuppressWarnings("unused")
public class Pago {

    @Id
    private Long id;

    @Column(name = "folio")
    private String folio;

    @Column(name = "boleta_id")
    private Long boletaId;

    @Column(name = "metodo_pago_id")
    private Long metodoPagoId;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "revertido_en")
    private LocalDateTime revertidoEn;

    protected Pago() {
    }
}
