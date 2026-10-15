package gt.edu.umg.desarrolloweb.consulta.config;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Reloj de la aplicacion. Las fechas de vencimiento de las boletas son fechas locales
 * (sin hora), asi que "hoy" se calcula en la zona horaria de Guatemala y no en UTC.
 * Tenerlo como bean permite fijar la fecha en las pruebas.
 */
@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("America/Guatemala"));
    }
}
