package gt.edu.umg.desarrolloweb.consulta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Immutable;

/**
 * Mapeo de SOLO LECTURA de la tabla {@code contadores} del monolito.
 * Se mapean unicamente las columnas que la consulta publica necesita: lo que no se mapea
 * (NIT, DPI, telefono, email...) no puede filtrarse por accidente. Se usa solo en consultas JPQL.
 */
@Entity
@Immutable
@Table(name = "contadores")
@SuppressWarnings("unused")
public class Contador {

    @Id
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "predio_id")
    private Long predioId;

    @Column(name = "paja_id")
    private Long pajaId;

    /** Columna ENUM de MySQL: se lee como texto con @Formula para que ddl-auto=validate no se queje del tipo. */
    @Formula("estado")
    private String estado;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected Contador() {
    }
}
