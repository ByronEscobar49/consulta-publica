package gt.edu.umg.desarrolloweb.consulta.repository;

import gt.edu.umg.desarrolloweb.consulta.entity.Contador;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

/**
 * Extiende {@link Repository} y no JpaRepository a proposito: asi NO existen save() ni delete().
 * Mas la regla de BD (usuario solo con SELECT), la escritura es imposible por dos caminos.
 */
public interface ContadorRepository extends Repository<Contador, Long> {

    /** Busca por codigo ignorando contadores borrados (borrado logico). La collation de MySQL ya ignora mayusculas. */
    @Query("""
            select new gt.edu.umg.desarrolloweb.consulta.repository.FichaContador(
                c.id, c.codigo, c.estado, cl.nombre, pj.nombre, s.nombre, pr.aldea, pr.zona, pr.calle, pr.numeroCasa)
            from Contador c
            join Cliente cl on cl.id = c.clienteId
            join Paja pj on pj.id = c.pajaId
            join Predio pr on pr.id = c.predioId
            left join Sector s on s.id = pr.sectorId
            where c.codigo = :codigo and c.deletedAt is null
            """)
    Optional<FichaContador> buscarFicha(@Param("codigo") String codigo);
}
