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
 * Mapeo de SOLO LECTURA de la tabla {@code boletas} del monolito.
 * Se mapean unicamente las columnas que la consulta publica necesita: lo que no se mapea
 * (NIT, DPI, telefono, email...) no puede filtrarse por accidente. Se usa solo en consultas JPQL.
 */
@Entity
@Immutable
@Table(name = "boletas")
@SuppressWarnings("unused")
public class Boleta {

    @Id
    private Long id;

    @Column(name = "folio")
    private String folio;

    @Column(name = "lectura_id")
    private Long lecturaId;

    @Column(name = "periodo_id")
    private Long periodoId;

    @Column(name = "consumo_m3")
    private BigDecimal consumoM3;

    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "fecha_emision")
    private LocalDate fechaEmision;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "anulada_en")
    private LocalDateTime anuladaEn;

    protected Boleta() {
    }
}
