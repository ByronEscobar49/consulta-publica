package gt.edu.umg.desarrolloweb.consulta.repository;

import gt.edu.umg.desarrolloweb.consulta.entity.Boleta;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BoletaRepository extends Repository<Boleta, Long> {

    /** Todas las boletas del contador (la boleta se liga al contador a traves de su lectura), la mas reciente primero. */
    @Query("""
            select new gt.edu.umg.desarrolloweb.consulta.repository.BoletaFila(
                b.id, b.folio, per.anio, per.mes, b.consumoM3, b.monto,
                b.fechaEmision, b.fechaVencimiento, b.anuladaEn)
            from Boleta b
            join Lectura l on l.id = b.lecturaId
            join Periodo per on per.id = b.periodoId
            where l.contadorId = :contadorId
            order by b.fechaEmision desc, b.id desc
            """)
    List<BoletaFila> boletasDelContador(@Param("contadorId") Long contadorId);
}
