package gt.edu.umg.desarrolloweb.consulta.repository;

import gt.edu.umg.desarrolloweb.consulta.entity.Pago;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface PagoRepository extends Repository<Pago, Long> {

    /** Lo pagado por boleta, sin contar pagos revertidos. */
    @Query("""
            select new gt.edu.umg.desarrolloweb.consulta.repository.PagadoPorBoleta(p.boletaId, sum(p.monto))
            from Pago p
            where p.boletaId in :boletaIds and p.revertidoEn is null
            group by p.boletaId
            """)
    List<PagadoPorBoleta> pagadoPorBoleta(@Param("boletaIds") Collection<Long> boletaIds);

    /** Ultimos pagos no revertidos del contador; el limite se pasa con PageRequest. */
    @Query("""
            select new gt.edu.umg.desarrolloweb.consulta.repository.PagoFila(p.folio, p.fechaPago, p.monto, m.nombre, b.folio)
            from Pago p
            join Boleta b on b.id = p.boletaId
            join Lectura l on l.id = b.lecturaId
            join MetodoPago m on m.id = p.metodoPagoId
            where l.contadorId = :contadorId and p.revertidoEn is null
            order by p.fechaPago desc, p.id desc
            """)
    List<PagoFila> ultimosPagos(@Param("contadorId") Long contadorId, Pageable pageable);
}
