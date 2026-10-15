package gt.edu.umg.desarrolloweb.consulta.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;

/**
 * Mapeo de SOLO LECTURA de la tabla {@code metodos_pago} del monolito.
 * Se mapean unicamente las columnas que la consulta publica necesita: lo que no se mapea
 * (NIT, DPI, telefono, email...) no puede filtrarse por accidente. Se usa solo en consultas JPQL.
 */
@Entity
@Immutable
@Table(name = "metodos_pago")
@SuppressWarnings("unused")
public class MetodoPago {

    @Id
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    protected MetodoPago() {
    }
}
