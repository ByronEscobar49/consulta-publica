package gt.edu.umg.desarrolloweb.consulta.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gt.edu.umg.desarrolloweb.consulta.repository.FichaContador;
import org.junit.jupiter.api.Test;

class TextoPublicoTest {

    @Test
    void titularSeAbreviaConservandoElPrimerNombre() {
        assertEquals("María F. L. C.", TextoPublico.titularAbreviado("María Fernanda López Castillo"));
    }

    @Test
    void titularConUnaSolaPalabraQuedaIgual() {
        assertEquals("Cooperativa", TextoPublico.titularAbreviado("  Cooperativa "));
    }

    @Test
    void titularNuloOVacioDaTextoVacio() {
        assertEquals("", TextoPublico.titularAbreviado(null));
        assertEquals("", TextoPublico.titularAbreviado("   "));
    }

    @Test
    void ubicacionCompleta() {
        FichaContador f = new FichaContador(1L, "CTR-0001", "activo", "x", "1 paja",
                "Sector Centro", null, "1", "4a calle", "5-20");
        assertEquals("Sector Centro, zona 1, 4a calle 5-20", TextoPublico.ubicacion(f));
    }

    @Test
    void ubicacionNoRepiteLaAldeaSiYaEstaEnElSector() {
        FichaContador f = new FichaContador(2L, "CTR-0003", "activo", "x", "1/2 paja",
                "Aldea El Porvenir", "El Porvenir", "0", null, "12");
        assertEquals("Aldea El Porvenir, zona 0, casa 12", TextoPublico.ubicacion(f));
    }

    @Test
    void ubicacionConDatosFaltantesNoDejaComasSueltas() {
        FichaContador f = new FichaContador(3L, "CTR-0009", "activo", "x", "1 paja",
                null, null, null, null, null);
        assertEquals("", TextoPublico.ubicacion(f));
    }
}
