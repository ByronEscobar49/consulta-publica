package gt.edu.umg.desarrolloweb.consulta.controller;

import gt.edu.umg.desarrolloweb.consulta.api.dto.EstadoDeCuentaDto;
import gt.edu.umg.desarrolloweb.consulta.api.spec.ConsultaApi;
import gt.edu.umg.desarrolloweb.consulta.service.ConsultaService;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementa la interfaz generada desde consulta-api.yaml. Las rutas y validaciones del
 * parametro vienen del contrato: aqui solo se delega en el servicio.
 */
@RestController
public class ConsultaController implements ConsultaApi {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @Override
    public ResponseEntity<EstadoDeCuentaDto> consultarEstadoDeCuenta(String codigoContador) {
        // no-store: es el estado de cuenta de alguien, no debe quedar en caches intermedias.
        return ResponseEntity.ok()
                .cacheControl(CacheControl.noStore())
                .body(consultaService.consultar(codigoContador));
    }
}
