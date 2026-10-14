package gt.edu.umg.desarrolloweb.consulta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de arranque de consulta-service. Va en el paquete raiz: el escaneo de componentes
 * busca controllers, services y repositories de aqui hacia abajo.
 */
@SpringBootApplication
public class ConsultaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsultaApplication.class, args);
    }
}
