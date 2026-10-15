package gt.edu.umg.desarrolloweb.consulta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 * Mapeo de SOLO LECTURA de la tabla {@code predios} del monolito.
 * Se mapean unicamente las columnas que la consulta publica necesita: lo que no se mapea
 * (NIT, DPI, telefono, email...) no puede filtrarse por accidente. Se usa solo en consultas JPQL.
 */
@Entity
@Immutable
@Table(name = "predios")
@SuppressWarnings("unused")
public class Predio {

    @Id
    private Long id;

    @Column(name = "sector_id")
    private Long sectorId;

    @Column(name = "aldea")
    private String aldea;

    @Column(name = "zona")
    private String zona;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero_casa")
    private String numeroCasa;

    protected Predio() {
    }
}
