package gt.edu.umg.desarrolloweb.consulta.ratelimit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClientIpTest {

    @Test
    void usaLaPrimeraIpDeXForwardedForSiSeConfia() {
        assertEquals("203.0.113.7", ClientIp.resolve("203.0.113.7, 10.0.0.2", "172.18.0.5", true));
    }

    @Test
    void sinConfianzaIgnoraLaCabeceraYUsaLaConexion() {
        assertEquals("172.18.0.5", ClientIp.resolve("203.0.113.7", "172.18.0.5", false));
    }

    @Test
    void sinCabeceraUsaLaConexion() {
        assertEquals("172.18.0.5", ClientIp.resolve(null, "172.18.0.5", true));
        assertEquals("172.18.0.5", ClientIp.resolve("  ", "172.18.0.5", true));
    }

    @Test
    void cabeceraConBasuraSeDescarta() {
        assertEquals("172.18.0.5", ClientIp.resolve("hola\nmundo", "172.18.0.5", true));
        assertEquals("172.18.0.5", ClientIp.resolve("<script>", "172.18.0.5", true));
    }

    @Test
    void aceptaIpv6() {
        assertEquals("2001:db8::1", ClientIp.resolve("2001:DB8::1", "172.18.0.5", true));
    }
}
